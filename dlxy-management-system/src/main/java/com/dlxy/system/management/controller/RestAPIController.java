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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTMLDocument.HTMLReader.ParagraphAction;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.dlxy.server.user.service.IUserService;
import com.dlxy.system.management.model.FormArticle;
import com.dlxy.system.management.model.FormTitle;
import com.dlxy.system.management.service.IArticleManagementWrappedService;
import com.dlxy.system.management.service.IPictureManagementWrappedService;
import com.dlxy.system.management.service.command.AddOrUpdateArtilceCommand;
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
	@Autowired
	private AddOrUpdateArtilceCommand addOrUpdateArtilceCommand;

	// @Autowired
	// private IArticleService articleService;

	@Autowired
	private ITitleService titleService;

	@Autowired
	private AppEventPublisher eventPublisher;

	@Autowired
	private IPictureService pictureService;

	@Autowired
	private IUserService userService;
	@Autowired
	private IArticleService articleService;

	@RequestMapping(value = "/article/del/batch")
	@ResponseBody
	public ResultDTO<String> delArticles(HttpServletRequest request, HttpServletResponse response)
	{
		UserDTO user = ManagementUtil.getLoginUser();
		String ids = request.getParameter("ids");
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
			articleManagementWrappedService.updateArticlesInBatch(user.getUserId(), idA,
					ArticleStatusEnum.DELETE.ordinal());
			return ResultUtil.sucess();
		} catch (Exception e)
		{
			e.printStackTrace();
			return ResultUtil.fail("error:" + e.getMessage());
		}
	}

	@RequestMapping(value = "/article/detail/{articleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<ArticleDTO> showArticleDetail(@PathVariable Long articleId, HttpServletRequest request,
			HttpServletResponse response)
	{
		try
		{
			ArticleDTO articleDTO = articleService.findArticleDetailByArticleId(articleId);
			return ResultUtil.sucess(articleDTO);
		} catch (Exception e)
		{
			logger.error("[find article] occur error {}", e.getMessage());
			e.printStackTrace();
			return ResultUtil.fail(e.getMessage());
		}
	}
	@RequestMapping(value="/article/delete",method= {RequestMethod.POST,RequestMethod.GET} ,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String> deleteArticleInBatch(HttpServletRequest request,HttpServletResponse response)
	{
		String articleIds=request.getParameter("articleId");
		Map<String, String>params=new HashMap<>();
		if(StringUtils.isEmpty(articleIds))
		{
			params.put("erro", "missing argument:articleId");
		}
		String[] ids = articleIds.split(",");
		Long[] idArr=new Long[ids.length];
		try
		{
			for(int i=0;i<ids.length;i++)
			{
				idArr[i]=Long.parseLong(ids[i]);
			}
		} catch (Exception e)
		{
			params.put("error", "illegal argument");
		}
	
		if(params.containsKey("error"))
		{
			return ResultUtil.fail(params.get("error"));
		}
		try
		{
			articleManagementWrappedService.deleteInBatch(ManagementUtil.getLoginUser().getUserId(), idArr);
			return ResultUtil.sucess();
		} catch (Exception e)
		{
			logger.error("[delete article] error {}",e.getMessage());
			e.printStackTrace();
			return ResultUtil.fail(e.getMessage());
		}
		
	}

	@RequestMapping(value = "/article/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public ResultDTO<String> updateArticle(FormArticle formArticle, BindingResult result, HttpServletRequest request,
			HttpServletResponse response)
	{
		ArticleDTO articleDTO = new ArticleDTO();
		articleDTO.setUserId(ManagementUtil.getLoginUser().getUserId());
		Map<String, Object> params = new HashMap<>();
		String[] pictureIds = request.getParameterValues("pictureId");
		String error = null;
		if (result.hasErrors())
		{
			error = result.getAllErrors().toString();
		}
		try
		{
			formArticle.isInvalid();
		} catch (Exception e)
		{
			error = e.getMessage();
		}
		if (!StringUtils.isEmpty(error))
		{
			return ResultUtil.fail(error);
		}
		formArticle.to(articleDTO);
		if (null != pictureIds && pictureIds.length > 0)
		{
			articleDTO.setPictureIds(pictureIds);
			params.put("pictureStatus", PictureStatusEnum.Effective.ordinal());
		}
		params.put("articleDTO", articleDTO);

		params.put("update", true);
		try
		{
			addOrUpdateArtilceCommand.execute(params);
			return ResultUtil.sucess();
		} catch (Exception e)
		{
			e.printStackTrace();
			return ResultUtil.fail(e.getMessage());
		}
	}

	/*
	 * username,articleId,articleName,
	 */
	@RequestMapping("/search/article")
	public ResultDTO<PageVO<Collection<ArticleDTO>>> searchArticle(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException
	{
		Map<String, Object> params = new HashMap<>();
		UserDTO user = ManagementUtil.getLoginUser();
		String q = new String(request.getParameter("q").getBytes("iso-8859-1"), "utf-8");
		String pageSizeStr = StringUtils.defaultString(request.getParameter("pageSize"), "2");
		String pageNumStr = StringUtils.defaultString(request.getParameter("pageNum"), "1");
		Integer pageSize = Integer.parseInt(pageSizeStr);
		Integer pageNum = Integer.parseInt(pageNumStr);
		if (StringUtils.isEmpty(q))
		{
			return ResultUtil.fail("参数不可为空");
		}
		String searchQuery = CommonUtils.validStringException(q.trim());
		String userIdStr = request.getParameter("userId");
		if (!StringUtils.isEmpty(userIdStr))
		{
			Long userId = Long.parseLong(userIdStr);

			// 查找用户,与登录着的用户进行匹配,判断是否是同一个用户.或者是admin
			if (!user.getUserId().equals(userId) && !user.isAdmin())
			{
				// 发布消息,记录ip ,额外创建一个对象更加合适,用于安全检测
				HashMap<String, Object> p = new HashMap<>();
				p.put("ip", CommonUtils.getRemortIP(request));
				p.put("reason", "try over access to get data");
				eventPublisher.publish(new AppEvent(p, Events.IpCheck.name()));
				// 返回无权
				return ResultUtil.fail("sorry u dont have the permission");
			}
			// UserDTO sUser=null;
			// try
			// {
			// sUser=userService.findByUserId(userId);
			// }catch (NumberFormatException e) {
			// logger.error("[rest api:搜索] string userId 转为 long 的userId 出现错误");
			// return ResultUtil.fail(e.getMessage());
			// }
			// catch (SQLException e)
			// {
			//// e.printStackTrace();
			// logger.error("[rest api :search] occur sql exception ,{}",e.getMessage());
			// return ResultUtil.fail("服务器异常,请稍后再试");
			// }
			params.put("userId", userIdStr);
		}
		try
		{
			ArticleDTO findByArticleId = articleManagementWrappedService.findByArticleId(Long.parseLong(searchQuery));
			return ResultUtil
					.sucess(new PageVO<Collection<ArticleDTO>>(Arrays.asList(findByArticleId), pageSize, pageNum, 1L));
		} catch (NumberFormatException e)
		{

			params.put("searchParam", searchQuery);
			try
			{
				PageDTO<Collection<ArticleDTO>> pageRes = articleManagementWrappedService.findByParams(pageSize,
						pageNum, params);
				return ResultUtil.sucess(new PageVO<Collection<ArticleDTO>>(pageRes.getData(), pageSize, pageNum,
						pageRes.getTotalCount()), "sucess");
			} catch (SQLException e1)
			{
				logger.error("[rest api :search 查询文章] find articleByParam occur sql exception");
				e1.printStackTrace();
				return ResultUtil.fail(e1.getMessage());
			}
		} catch (SQLException e)
		{
			logger.error("[rest api :search 查询文章] find articleById occur sql exception");
			e.printStackTrace();
			return ResultUtil.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "/article/update/status", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String> updateArticleStatus(HttpServletRequest request, HttpServletResponse response)
	{
		String[] articleIds = request.getParameterValues("articleId");
		if (null == articleIds || articleIds.length < 1)
		{
			return ResultUtil.fail("missing argument:articleId");
		}
		String articleStatusStr = request.getParameter("articleStatus");
		int artilceStatus = 0;
		Long[] ids = new Long[articleIds.length];
		try
		{
			for (int i = 0; i < articleIds.length; i++)
			{
				ids[i] = Long.parseLong(articleIds[i]);
			}
			artilceStatus = Integer.parseInt(articleStatusStr);
			if (artilceStatus < 0 || artilceStatus > 3)
			{
				throw new RuntimeException("文章状态超出范围");
			}
		} catch (Exception e)
		{
			return ResultUtil.fail("illegal argument" + e.getMessage());
		}
		try
		{
			articleManagementWrappedService.updateArticlesInBatch(ManagementUtil.getLoginUser().getUserId(), ids,
					artilceStatus);
			return ResultUtil.sucess();
		} catch (Exception e)
		{
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
		DlxyTitleDTO titleDTO = null;
		String statusStr = StringUtils.defaultString(request.getParameter("status"), "0");

		String titleIdStr = request.getParameter("titleId");

		if (!StringUtils.isEmpty(titleIdStr))
		{
		}

		/*
		 * 1.先通过articleId 查询类目和文章的状态,文章如果不是处于2的状态,则直接返回结果 2.如果存在则直接更新状态, 如果不存在
		 * 1.先查询article 是否存在 不存在直接返回失败 2.存在再进行状态判断 状态不对直接返回失败 3.查询类目是否还存在 存在直接更新信息
		 */
		HashMap<String, Object> data = new HashMap<>();
		try
		{
			ArticleDTO articleDTO = articleManagementWrappedService.findByArticleId(articleId);

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
			int i = articleManagementWrappedService.rollBackArticle(user.getUserId(), Integer.parseInt(statusStr),
					articleDTO.getArticleId(), articleDTO.getTitleId());
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

	@RequestMapping(value = "/article/update/{articleId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<?> updateArticle(@PathVariable Long articleId, HttpServletRequest request,
			HttpServletResponse response)
	{

		return null;

	}

	/**
	 * @param parentId
	 *            父类的id
	 * @param request
	 * @param response
	 * @return 返回这个父类下的所有子类
	 * @author joker
	 * @date 创建时间：2018年7月7日 下午2:24:52
	 */
	@RequestMapping(value = "/title/{parentId}", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<Collection<DlxyTitleDTO>> findTitles(@PathVariable Integer parentId, HttpServletRequest request,
			HttpServletResponse response)
	{
		Collection<DlxyTitleDTO> collection = titleService.findChildsByParentId(parentId);
		if(null==collection || collection.isEmpty())
		{
			return ResultUtil.needMoreOp(collection,"无子类目");
		}
		return ResultUtil.sucess(collection);
	}

	@RequestMapping(value = "/titles", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<Collection<DlxyTitleDTO>> findParentAllChilds(HttpServletRequest request,
			HttpServletResponse response)
	{
		try
		{
			Collection<DlxyTitleDTO> collection = titleService.findAllParent();
			return ResultUtil.sucess(collection);
		} catch (Exception e)
		{
			return ResultUtil.fail(e.getMessage());
		}
	}
	@RequestMapping(value="/title/addOrUpdate",produces=MediaType.APPLICATION_JSON_UTF8_VALUE,consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE,method=RequestMethod.POST)
	public ResultDTO<String>addOrUpdateTitle(FormTitle formTitle,BindingResult result,HttpServletRequest request,HttpServletResponse response)
	{
		
//		if(result.hasErrors())
//		{
//			return ResultUtil.fail(result.getAllErrors().toString());
//		}
		try
		{
			CommonUtils.validStringException(formTitle.getTitleName());
			DlxyTitleDTO dlxyTitleDTO=new DlxyTitleDTO();
			formTitle.to(dlxyTitleDTO);
			articleManagementWrappedService.addTitleOrUpdate(ManagementUtil.getLoginUser().getUserId(), dlxyTitleDTO);
			return ResultUtil.sucess();
		} catch (Exception e)
		{
			e.printStackTrace();
			return ResultUtil.fail(e.getMessage());
		}
	}
	@RequestMapping(value="/title/delete",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String>deleteTitle(HttpServletRequest request,HttpServletResponse response)
	{
		String titleIdStr = request.getParameter("titleId");
		Integer titleId=null;
		if(StringUtils.isEmpty(titleIdStr))
		{
			return ResultUtil.fail("missing argument:titleId");
		}
		try
		{
			titleId=Integer.parseInt(titleIdStr);
			articleManagementWrappedService.deleteByTitleId(ManagementUtil.getLoginUser().getUserId(), titleId);
			return ResultUtil.sucess();
		} catch (Exception e)
		{
			logger.error("[delete title] error: {}",e.getMessage());
			e.printStackTrace();
			return ResultUtil.fail("error:"+e.getMessage());
		}
	
	}

	@RequestMapping(value = "/picture/delete", method =
	{ RequestMethod.POST,
			RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String>deletePic(HttpServletRequest request,HttpServletResponse httpServletResponse)
	{
		String pictureIdStr = request.getParameter("pictureId");
		String pictureType=request.getParameter("pictureType");
		if(StringUtils.isEmpty(pictureIdStr)|| StringUtils.isEmpty(pictureType))
		{
			return ResultUtil.fail("missing argument:pictureId or pictureType");
		}
		try
		{
			List<Long>pictureIds=new ArrayList<Long>();
			String[] split = pictureIdStr.split(",");
			for (String string : split)
			{
				pictureIds.add(Long.valueOf(string));
			}
			pictureManagementWrappedService.deletePicture(ManagementUtil.getLoginUser().getUserId(), pictureIds, Integer.parseInt(pictureType));
			return ResultUtil.sucess("删除成功");
		} catch (Exception e)
		{
			return ResultUtil.fail("error:"+e.getMessage());
		}
	}

	@RequestMapping(value = "/file/upload")
	public PictureUploadResponseDTO uploadFile(@RequestParam("imgFile") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response)
	{
		// MultipartRequest req=(MultipartRequest) request;
		PictureUploadResponseDTO pictureUploadResponseDTO = new PictureUploadResponseDTO();
		String articleId = request.getParameter("articleId");
		articleId = articleId.replaceAll(",", "");
		if (StringUtils.isEmpty(articleId))
		{
			pictureUploadResponseDTO.setError(0);
			pictureUploadResponseDTO.setMessage("缺失参数,请刷新页面重试");
			return pictureUploadResponseDTO;
		}
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
		String realPath = request.getServletContext().getRealPath("");
		File dirFile = new File(realPath + File.separator + "imgs" + File.separator + articleId + File.separator);
		if (!dirFile.exists())
		{
			dirFile.mkdirs();
		}
		String fileName = UUID.randomUUID().toString();

		File newFiel = new File(dirFile.getAbsolutePath() + File.separator + fileName + suffix);
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

		StringBuffer reqUrl = request.getRequestURL();
		String requestURI = request.getRequestURI();
		String string = reqUrl.substring(0, reqUrl.indexOf(requestURI));
		// System.out.println(string+File.separator+"imgs"+File.separator+articleId+File.separator+fileName);
		String url = string + File.separator + "imgs" + File.separator + articleId + File.separator + fileName + suffix;
		PictureDTO pictureDTO = new PictureDTO();
//		pictureDTO.setPictureId(fileName);
		pictureDTO.setPictureUrl(url);
		pictureDTO.setArticleId(Long.parseLong(articleId));
		pictureDTO.setCreateDate(new Date());
		pictureDTO.setPictureType(PictureTypeEnum.Article_Picture.ordinal());
		pictureDTO.setPictureStatus(PictureStatusEnum.Invalid.ordinal());
		try
		{
			// 这里上面需要进行设置,使之成为一个集合,至于保存采用多线程的方式
			// pictureService.addPicture(new PictureDTO[] {pictureDTO});
			// pictureService.addPictureWithArticleId(Long.parseLong(articleId), new
			// PictureDTO[] {pictureDTO});
			pictureManagementWrappedService.addPciture(ManagementUtil.getLoginUser().getUserId(),
					Long.parseLong(articleId), new PictureDTO[]
					{ pictureDTO });
			System.out.println(pictureDTO.getPictureId());
			pictureUploadResponseDTO.setError(0);
			pictureUploadResponseDTO.setUrl(url+"?pictureId="+pictureDTO.getPictureId());
		} catch (SQLException e)
		{
			e.printStackTrace();
			pictureUploadResponseDTO.setError(1);
			pictureUploadResponseDTO.setMessage(e.getMessage());
		}
		return pictureUploadResponseDTO;
	}
}
