/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月14日 下午6:51:17
* 
*/
package com.dlxy.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.ResultDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.common.enums.DlxyTitleEnum;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.common.event.AppEvent;
import com.dlxy.common.utils.ResultUtil;
import com.dlxy.common.vo.PageVO;
import com.dlxy.exception.DlxySystemIllegalException;
import com.dlxy.model.FormArticle;
import com.dlxy.model.FormTitle;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.service.IArticleManagementWrappedService;
import com.dlxy.service.IPictureManagementWrappedService;
import com.dlxy.service.command.AddOrUpdateArtilceCommand;
import com.dlxy.utils.AdminUtil;
import com.joker.library.utils.CommonUtils;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月14日 下午6:51:17
 */
@RestController
@RequestMapping("/api/v1")
public class RestAPIController
{
	// @Autowired
	// private ITitleManagementWrappedService titleManagementWrappedServiceImpl;

	private Logger logger = LoggerFactory.getLogger(RestAPIController.class);
	@Autowired
	private IArticleManagementWrappedService articleManagementWrappedService;
	@Autowired
	private ITitleService titleService;
	@Autowired
	private IArticleService articleService;
	@Autowired
	private IPictureManagementWrappedService pictureManagementWrappedService;
	@Autowired
	private AddOrUpdateArtilceCommand addOrUpdateArtilceCommand;


	@RequestMapping(value = "/admin/title/addOrUpdate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String> addOrUpdateTitle(FormTitle formTitle, HttpServletRequest request,
			HttpServletResponse response)
	{
		Map<String, String> params = new HashMap<String, String>();
		if (CommonUtils.validString(formTitle.getTitleName()))
		{
			params.put("error", "类型名称不合法,请重新输入");
		}
		DlxyTitleDTO titleDTO = new DlxyTitleDTO();
		formTitle.to(titleDTO);
		UserDTO userDTO = AdminUtil.getLoginUser();
		try
		{
			articleManagementWrappedService.addTitleOrUpdate(userDTO, titleDTO);
		} catch (Exception e)
		{
			logger.error("[addOrUpdate title ] occur error {}", e.getMessage());
			return ResultUtil.fail(e.getMessage());
		}
		return ResultUtil.sucess();
	}

	/*
	 * limited condition: username,articleId,articleName,titleId searchCondition:
	 * userName,articleName
	 */
	@RequestMapping("/search/article")
	public ResultDTO<PageVO<Collection<ArticleDTO>>> searchArticle(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		Map<String, Object> params = new HashMap<>();
		UserDTO user = AdminUtil.getLoginUser();
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
		String titleIdStr = request.getParameter("titleId");
		String parentTitleIdStr = request.getParameter("titleParentId");
		if (!StringUtils.isEmpty(titleIdStr))
		{
			params.put("titleId", titleIdStr);
		} else if (!StringUtils.isEmpty(parentTitleIdStr))
		{
			params.put("titleParentId", parentTitleIdStr);
		}

		if (!StringUtils.isEmpty(userIdStr))
		{
			Long userId = Long.parseLong(userIdStr);

			// 查找用户,与登录着的用户进行匹配,判断是否是同一个用户.或者是admin
			if (!user.getUserId().equals(userId) && !user.isAdmin())
			{
				// 发布消息,记录ip ,额外创建一个对象更加合适,用于安全检测
				// HashMap<String, Object> p = new HashMap<>();
				// p.put("ip", CommonUtils.getRemortIP(request));
				// p.put("reason", "try over access to get data");
				// eventPublisher.publish(new AppEvent(p, Events.IpCheck.name()));
				// 返回无权
				IllegalLogDTO illegalLogDTO = new IllegalLogDTO(CommonUtils.getRemortIP(request), user.getUserId(),
						"试图跨权访问" + userId + "记录", IllegalLevelEnum.Suspicious.ordinal());
				response.getWriter().write("sorry u dont have the permission");
				throw new DlxySystemIllegalException(illegalLogDTO);
			}
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
		if (null == collection || collection.isEmpty())
		{
			return ResultUtil.needMoreOp(collection, "无子类目");
		}
		return ResultUtil.sucess(collection);
	}

	@RequestMapping(value = "/aboutTitles", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<Collection<DlxyTitleDTO>> findParentAllChilds(HttpServletRequest request,
			HttpServletResponse response)
	{
		try
		{
			Collection<DlxyTitleDTO> collection = titleService.findTitlesByType(DlxyTitleEnum.UP_TITLE.ordinal());
			return ResultUtil.sucess(collection);
		} catch (Exception e)
		{
			return ResultUtil.fail(e.getMessage());
		}
	}
	@RequestMapping(value="/titles",method= {RequestMethod.POST,RequestMethod.GET},produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<Collection<DlxyTitleDTO>>findAllTitles()
	{
		try
		{
			Collection<DlxyTitleDTO> collection = titleService.findTitlesByType(-1);
			return ResultUtil.sucess(collection);
		} catch (Exception e)
		{
			return ResultUtil.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "/title/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String> deleteTitle(HttpServletRequest request, HttpServletResponse response)
	{
		String titleIdStr = request.getParameter("titleId");
		Integer titleId = null;
		if (StringUtils.isEmpty(titleIdStr))
		{
			return ResultUtil.fail("missing argument:titleId");
		}
		try
		{
			titleId = Integer.parseInt(titleIdStr);
			articleManagementWrappedService.deleteByTitleId(AdminUtil.getLoginUser(), titleId);
			return ResultUtil.sucess();
		} catch (Exception e)
		{
			logger.error("[delete title] error: {}", e.getMessage());
			e.printStackTrace();
			return ResultUtil.fail("error:" + e.getMessage());
		}
	}

	@RequestMapping(value = "/article/del/batch")
	@ResponseBody
	public ResultDTO<String> delArticles(HttpServletRequest request, HttpServletResponse response)
	{
		UserDTO user = AdminUtil.getLoginUser();
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
			articleManagementWrappedService.updateArticlesInBatch(user, idA, ArticleStatusEnum.DELETE.ordinal());
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

	@RequestMapping(value = "/article/delete", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String> deleteArticleInBatch(HttpServletRequest request, HttpServletResponse response)
	{
		String articleIds = request.getParameter("articleId");
		Map<String, String> params = new HashMap<>();
		if (StringUtils.isEmpty(articleIds))
		{
			params.put("erro", "missing argument:articleId");
		}
		String[] ids = articleIds.split(",");
		Long[] idArr = new Long[ids.length];
		try
		{
			for (int i = 0; i < ids.length; i++)
			{
				idArr[i] = Long.parseLong(ids[i]);
			}
		} catch (Exception e)
		{
			params.put("error", "illegal argument");
		}

		if (params.containsKey("error"))
		{
			return ResultUtil.fail(params.get("error"));
		}
		try
		{
			articleManagementWrappedService.deleteInBatch(AdminUtil.getLoginUser(), idArr);
			return ResultUtil.sucess();
		} catch (Exception e)
		{
			logger.error("[delete article] error {}", e.getMessage());
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
			articleManagementWrappedService.updateArticlesInBatch(AdminUtil.getLoginUser(), ids, artilceStatus);
			return ResultUtil.sucess();
		} catch (Exception e)
		{
			return ResultUtil.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "/article/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public ResultDTO<String> updateArticle(FormArticle formArticle, BindingResult result, HttpServletRequest request,
			HttpServletResponse response)
	{
		String[] titleIdStrs=request.getParameterValues("titleId");
		Arrays.sort(titleIdStrs,new Comparator<String>()
		{
			@Override
			public int compare(String o1, String o2)
			{
				int i1=Integer.valueOf(o1);
				int i2=Integer.valueOf(o2);
				return i1<i2?1:-1;
			}
		});
		formArticle.setTitleId(Integer.parseInt(titleIdStrs[0]));
		ArticleDTO articleDTO = new ArticleDTO();
		articleDTO.setUserId(AdminUtil.getLoginUser().getUserId());
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

}
