/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 上午8:49:22
* 
*/
package com.dlxy.system.management.controller;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.PictureDTO;
import com.dlxy.common.dto.PictureUploadResponseDTO;
import com.dlxy.common.dto.ResultDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.common.enums.PictureTypeEnum;
import com.dlxy.common.event.AppEvent;
import com.dlxy.common.event.AppEventPublisher;
import com.dlxy.common.event.Events;
import com.dlxy.common.utils.ResultUtil;
import com.dlxy.common.vo.PageVO;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.server.picture.service.IPictureService;
import com.dlxy.system.management.service.IArticleManagementWrappedService;
import com.dlxy.system.management.service.IPictureManagementWrappedService;
import com.dlxy.system.management.utils.ManagementUtil;
import com.joker.library.utils.CommonUtils;
import com.sun.xml.internal.rngom.util.Uri;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月29日 上午8:49:22
 */
@RestController
@RequestMapping("/api")
public class RestAPIController
{
	private static Logger logger = LoggerFactory.getLogger(RestAPIController.class);

	@Autowired
	private IArticleManagementWrappedService articleManagementWrappedService;
	@Autowired
	private IPictureManagementWrappedService pictureManagementWrappedService;

//	@Autowired
//	private IArticleService articleService;

	@Autowired
	private ITitleService titleService;

	@Autowired
	private AppEventPublisher eventPublisher;
	
	@Autowired
	private IPictureService pictureService;
	

	@RequestMapping(value = "/article/del/batch")
	@ResponseBody
	public ResultDTO<String> delArticles(HttpServletRequest request, HttpServletResponse response)
	{
		UserDTO user = ManagementUtil.getLoginUser();
		String ids = request.getParameter("ids");
		ids=ids.replaceAll(",", "");
		if (StringUtils.isEmpty(ids))
		{
			return ResultUtil.fail("missing argument");
		}
		String[] idArr = ids.split(",");
		try
		{
			Long[] idA = new Long[idArr.length];
			for (int i = 0; i < idArr.length; i++)
			{
				idA[i] = Long.valueOf(idArr[i]);
			}
			articleManagementWrappedService.updateArticlesInBatch(user.getUserId(),idA, ArticleStatusEnum.DELETE.ordinal());
			return ResultUtil.sucess();
		} catch (Exception e)
		{
			e.printStackTrace();
			return ResultUtil.fail("error:" + e.getMessage());
		}
	}

	/*
	 * username,articleId,articleName,
	 */
	@RequestMapping("/search/article")
	public ResultDTO<PageVO<Collection<ArticleDTO>>> searchArticle(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException
	{
		String q =new String( request.getParameter("q").getBytes("iso-8859-1"),"utf-8");
		String pageSizeStr = StringUtils.defaultString(request.getParameter("pageSize"), "2");
		String pageNumStr = StringUtils.defaultString(request.getParameter("pageNum"), "1");
		Integer pageSize = Integer.parseInt(pageSizeStr);
		Integer pageNum = Integer.parseInt(pageNumStr);
		if (StringUtils.isEmpty(q))
		{
			return ResultUtil.fail("参数不可为空");
		}
		String searchQuery = CommonUtils.validStringException(q.trim());
		try
		{
			Collection<ArticleDTO> findByArticleId = articleManagementWrappedService.findByArticleId(Long.parseLong(searchQuery));
			return ResultUtil.sucess(new PageVO<Collection<ArticleDTO>>(findByArticleId, pageSize, pageNum, 1L));
		} catch (NumberFormatException e)
		{
			Map<String, Object> params = new HashMap<>();
			params.put("searchParam", searchQuery);
			try
			{
				PageDTO<Collection<ArticleDTO>> pageRes = articleManagementWrappedService.findByParams((pageNum - 1) * pageSize,
						pageSize, params);
				return ResultUtil.sucess(new PageVO<Collection<ArticleDTO>>(pageRes.getData(), pageSize, pageNum,
						pageRes.getTotalCount()), "sucess");
			} catch (SQLException e1)
			{
				logger.error("[查询文章]发生sql错误");
				e1.printStackTrace();
				return ResultUtil.fail(e1.getMessage());
			}
		} catch (SQLException e)
		{
			logger.error("[查询文章]发生sql错误");
			e.printStackTrace();
			return ResultUtil.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "/article/rollback/{articleId}", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String> rollBackArticle(@PathVariable("articleId") Long articleId, HttpServletRequest request,
			HttpServletResponse response)
	{
		UserDTO user = ManagementUtil.getLoginUser();
		UserDTO loginUser = ManagementUtil.getLoginUser();
		String statusStr = StringUtils.defaultString(request.getParameter("status"), "0");
		/*
		 * 1.先通过articleId 查询类目和文章的状态,文章如果不是处于2的状态,则直接返回结果 2.如果存在则直接更新状态, 如果不存在
		 * 1.先查询article 是否存在 不存在直接返回失败 2.存在再进行状态判断 状态不对直接返回失败 3.查询类目是否还存在 存在直接更新信息
		 */
		HashMap<String, Object> data = new HashMap<>();
		try
		{
			Collection<ArticleDTO> collection = articleManagementWrappedService.findByArticleId(articleId);
			ArticleDTO articleDTO = collection.iterator().next();
			if (null == articleDTO || !articleDTO.getArticleStatus().equals(ArticleStatusEnum.DELETE.ordinal()))
			{
				// 发布消息,记录到illegal_log 中,然后后台线程自动更新redis(达到一定次数) 中的黑名单记录
				logger.warn("illegal operation at {} with the ip {} user:{}", new Date(), loginUser.getUserId());
				data.put("userId", loginUser.getUserId());
				data.put("illegalDetail", "试图恢复正常状态下的文章或者试图恢复不存在的文章");
				data.put("illegalLevel", IllegalLevelEnum.Suspicious.ordinal());
				eventPublisher.publish(new AppEvent(data, Events.UserIllegalLog.name()));
				return ResultUtil.fail("文章处于正常状态,现已记录您的信息");
			}
			int i = articleManagementWrappedService.rollBackArticle(user.getUserId(),Integer.parseInt(statusStr), articleDTO.getArticleId(),
					articleDTO.getTitleId());
			if (i < 1)
			{
				return ResultUtil.needMoreOp();
			} else
			{
				return ResultUtil.sucess();
			}
			// select * from dlxy_article where exists(select 1 from dlxy_title b where
			// b.title_id = 1 ) and article_id =3 ;
		} catch (SQLException e)
		{
			e.printStackTrace();
			return ResultUtil.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "/titles", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<Collection<DlxyTitleDTO>> findTitles(HttpServletRequest request, HttpServletResponse response)
	{
		String parentIdStr = request.getParameter("parentId");
		if (StringUtils.isEmpty(parentIdStr))
		{
			return ResultUtil.fail("错误的参数:titleId");
		}
		Collection<DlxyTitleDTO> collection = titleService.findByParentId(Integer.parseInt(parentIdStr));
		return ResultUtil.sucess(collection);
	}

	@RequestMapping(value = "/file/upload")
	public PictureUploadResponseDTO uploadFile(@RequestParam("imgFile") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response)
	{
		MultipartRequest req=(MultipartRequest) request;
		PictureUploadResponseDTO pictureUploadResponseDTO = new PictureUploadResponseDTO();
		String articleId=request.getParameter("articleId");
		articleId=articleId.replaceAll(",", "");
		if(StringUtils.isEmpty(articleId))
		{
			pictureUploadResponseDTO.setError(0);
			pictureUploadResponseDTO.setMessage("缺失参数,请刷新页面重试");
			return pictureUploadResponseDTO;
		}
		String suffix=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
		String realPath = request.getServletContext().getRealPath("");
		File dirFile=new File(realPath+File.separator+"imgs"+File.separator+articleId+File.separator);
		if(!dirFile.exists())
		{
			dirFile.mkdirs();
		}
		String fileName=UUID.randomUUID().toString();
		
		File newFiel=new File(dirFile.getAbsolutePath()+File.separator+fileName+suffix);
		try
		{
			file.transferTo(newFiel);
		} catch (IllegalStateException | IOException e)
		{
			e.printStackTrace();
			pictureUploadResponseDTO.setError(1);
			pictureUploadResponseDTO.setMessage(e.getMessage());
			return pictureUploadResponseDTO;
		}
		
		StringBuffer reqUrl=request.getRequestURL();
		String requestURI = request.getRequestURI();
		String string = reqUrl.substring(0,reqUrl.indexOf(requestURI));
//		System.out.println(string+File.separator+"imgs"+File.separator+articleId+File.separator+fileName);
		String url=string+File.separator+"imgs"+File.separator+articleId+File.separator+fileName+suffix;
		PictureDTO pictureDTO=new PictureDTO();
		pictureDTO.setPictureId(fileName);
		pictureDTO.setPictureUrl(url);
		pictureDTO.setArticleId(Long.parseLong(articleId));
		pictureDTO.setCreateDate(new Date());
		pictureDTO.setPictureType(PictureTypeEnum.Article_Picture.ordinal());
		pictureDTO.setPictureStatus(PictureStatusEnum.Invalid.ordinal());
		try
		{
			//这里上面需要进行设置,使之成为一个集合,至于保存采用多线程的方式
//			pictureService.addPicture(new PictureDTO[] {pictureDTO});
//			pictureService.addPictureWithArticleId(Long.parseLong(articleId), new PictureDTO[] {pictureDTO});
			pictureManagementWrappedService.addPciture(ManagementUtil.getLoginUser().getUserId(), Long.parseLong(articleId), new PictureDTO[] {pictureDTO});
			pictureUploadResponseDTO.setError(0);
			pictureUploadResponseDTO.setUrl(url);
		} catch (SQLException e)
		{
			e.printStackTrace();
			pictureUploadResponseDTO.setError(1);
			pictureUploadResponseDTO.setMessage(e.getMessage());
		}
		return pictureUploadResponseDTO;
	
	}
}
