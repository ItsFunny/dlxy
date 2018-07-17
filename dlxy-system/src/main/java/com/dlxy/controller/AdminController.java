/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月14日 下午6:51:43
* 
*/
package com.dlxy.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.common.enums.DlxyTitleEnum;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.common.service.IdWorkerService;
import com.dlxy.common.vo.PageVO;
import com.dlxy.exception.DlxySystemIllegalException;
import com.dlxy.model.FormArticle;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.service.IArticleManagementWrappedService;
import com.dlxy.service.IUserMangementWrappedService;
import com.dlxy.service.command.AddOrUpdateArtilceCommand;
import com.dlxy.utils.AdminUtil;
import com.joker.library.utils.CommonUtils;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月14日 下午6:51:43
 */
@Controller
@RequestMapping("/admin")
public class AdminController
{
	private Logger logger = LoggerFactory.getLogger(AdminController.class);
	@Autowired
	private ITitleService titleService;
	@Autowired
	private IArticleManagementWrappedService articleManagementWrappedService;
	@Autowired
	private AddOrUpdateArtilceCommand articleCommand;
	@Autowired
	private IUserMangementWrappedService userManagementWrappedService;

	@Autowired
	private IdWorkerService idWorkService;

	@RequestMapping("/test")
	public ModelAndView test(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("test");
		return modelAndView;
	}

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response)
	{
		UserDTO user = AdminUtil.getLoginUser();
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("user", user);
		return modelAndView;
	}

	/*
	 * 显示学院相关的类目
	 */
	@RequestMapping("/aboutTitles")
	public ModelAndView showAllTiltles(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("about_titles");
		// Collection<DlxyTitleDTO> collection = titleService.findAllParent();
		// modelAndView.addObject("titles",collection);
		return modelAndView;
	}

	/*
	 * 显示新闻相关的类目
	 */
	@RequestMapping("/newsTitles")
	public ModelAndView showAllNewsTitles(HttpServletRequest requestq, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("news_titles");
		Collection<DlxyTitleDTO> collection = titleService.findTitlesByType(DlxyTitleEnum.NEWS_TITLE.ordinal());
		DlxyTitleDTO dlxyTitleDTO = null;
		if (null != collection && !collection.isEmpty())
		{
			dlxyTitleDTO = collection.iterator().next();
			Collection<DlxyTitleDTO> childs = titleService.findChildsByParentId(dlxyTitleDTO.getTitleId());
			modelAndView.addObject("titles", childs);
		}
		modelAndView.addObject("title", dlxyTitleDTO);
		return modelAndView;
	}

	@RequestMapping("/title/article/{titleId}")
	public ModelAndView showTitleAllTiltles(@PathVariable("titleId") String titleIdStr, HttpServletRequest request,
			HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", AdminUtil.getLoginUser());
		String pageSizeStr = StringUtils.defaultString(request.getParameter("pageSize"), "5");
		String pageNumStr = StringUtils.defaultString(request.getParameter("pageNum"), "1");
		Integer pageSize = Integer.parseInt(pageSizeStr);
		Integer pageNum = Integer.parseInt(pageNumStr);
		Integer titleId = null;
		try
		{
			titleId = Integer.parseInt(titleIdStr);
			Collection<DlxyTitleDTO> collection = titleService.findChildsByParentId(titleId);
			List<Integer> ids = new ArrayList<>();
			if (null != collection && !collection.isEmpty())
			{

				collection.stream().forEach(p -> {
					ids.add(p.getTitleId());
				});
				params.put("titleParentId", titleId);
			} else
			{
				ids.add(titleId);
				params.put("titleIdStr", titleId);
			}

			PageDTO<Collection<ArticleDTO>> pageDTO = articleManagementWrappedService.findByTitleIds(pageSize, pageNum,
					ids);
			PageVO<Collection<ArticleDTO>> pageVO = new PageVO<Collection<ArticleDTO>>(pageDTO.getData(), pageSize,
					pageNum, pageDTO.getTotalCount());
			params.put("pageVO", pageVO);
		} catch (Exception e)
		{
			logger.error("[show articles belong to the title ] titleId:{} ,error:{},location:{}", titleId,
					e.getMessage(), e.getStackTrace());
			params.put("error", e.getMessage());
		}
		if (params.containsKey("error"))
		{
			modelAndView = new ModelAndView("error", params);
		} else
		{
			modelAndView = new ModelAndView("title_article_detail", params);
		}
		return modelAndView;
	}

	/*
	 * article
	 */
	@RequestMapping("/articles")
	public ModelAndView showAllArticles(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		Map<String, Object> params = new HashMap<>();
		UserDTO user = AdminUtil.getLoginUser();
		Long userId = user.getUserId();
		boolean isIllegal = false;
		int pageSize = Integer.parseInt(StringUtils.defaultString(request.getParameter("pageSize"), "5"));
		int pageNum = Integer.parseInt(StringUtils.defaultString(request.getParameter("pageNum"), "1"));
		String userIdStr = request.getParameter("userId");
		String type = StringUtils.defaultString(request.getParameter("type"), "");
		if (type.equals("news"))
		{
			params.put("articleStatus", ArticleStatusEnum.INTRODUCE.ordinal());
		} else if (type.equals("deleted"))
		{
			params.put("articleStatus", ArticleStatusEnum.DELETE.ordinal());
		} else if (type.equals("all"))
		{
			if (!user.isAdmin())
			{
				isIllegal = true;
			}
		} else
		{
			// show ownself
			if (!StringUtils.isEmpty(userIdStr))
			{
				userId = Long.parseLong(userIdStr);
			}
			if (user.isAdmin() || user.getUserId().equals(userId))
			{
				params.put("userId", userId);
			} else
			{
				isIllegal = true;
			}
		}
		if (isIllegal)
		{
			IllegalLogDTO illegalLogDTO = new IllegalLogDTO(CommonUtils.getRemortIP(request), user.getUserId(),
					"试图跨权访问所有文章", IllegalLevelEnum.Suspicious.ordinal());
			throw new DlxySystemIllegalException(illegalLogDTO);
		}
		try
		{
			PageDTO<Collection<ArticleDTO>> pageDTO = articleManagementWrappedService.findByParams(pageSize, pageNum,
					params);
			PageVO<Collection<ArticleDTO>> pageVO = new PageVO<Collection<ArticleDTO>>(pageDTO.getData(), pageSize,
					pageNum, pageDTO.getTotalCount());
			params.put("pageVO", pageVO);
			params.put("user", user);
			params.put("type", type);
			modelAndView = new ModelAndView("articles", params);
			return modelAndView;
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("[show articles],params:{} reason:", params, e.getMessage());
			modelAndView = new ModelAndView("error", params);
			return modelAndView;
		}
	}

	@RequestMapping("/article/add")
	public ModelAndView addArticle(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response)
	{
		UserDTO user = AdminUtil.getLoginUser();
		long articleId = idWorkService.nextId();
		params.put("articleId", articleId);
		params.put("user", user);
		ModelAndView modelAndView = new ModelAndView("article_add", params);
		return modelAndView;
	}

	@RequestMapping("/article/doAddArticle")
	public ModelAndView doAddArticle(@Valid FormArticle formArticle, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		String[] pictureIds = request.getParameterValues("pictureId");
		Map<String, Object> params = new HashMap<>();
		if (bindingResult.hasErrors())
		{
			params.put("error", bindingResult.getAllErrors().toString());
		}
		String[] titleIds = request.getParameterValues("titleId");
		Arrays.sort(titleIds, new Comparator<String>()
		{

			@Override
			public int compare(String o1, String o2)
			{
				return Integer.parseInt(o1) < Integer.parseInt(o2) ? 1 : -1;
			}
		});
		try
		{
			formArticle.setTitleId(Integer.parseInt(titleIds[0]));
			formArticle.isInvalid();
		} catch (Exception e)
		{
			e.printStackTrace();
			params.put("error", e.getMessage());
		}
		if (params.containsKey("error"))
		{
			modelAndView = new ModelAndView("error", params);
			return modelAndView;
		}
		ArticleDTO articleDTO = new ArticleDTO();
		formArticle.to(articleDTO);

		articleDTO.setUserId(AdminUtil.getLoginUser().getUserId());
		articleDTO.setRealname(AdminUtil.getLoginUser().getRealname());
		articleDTO.setPictureIds(pictureIds);
		// PictureDTO pictureDTO=new PictureDTO();
		// pictureDTO.setArticleId(articleDTO.getArticleId());
		// pictureDTO.setPictureStatus(PictureStatusEnum.Effective.ordinal());
		params.put("articleDTO", articleDTO);
		// params.put("pictureDTO", pictureDTO);
		params.put("pictureStatus", PictureStatusEnum.Effective.ordinal());
		try
		{
			articleCommand.execute(params);
			modelAndView = new ModelAndView("redirect:/article/all.html");
			modelAndView.addObject("error", "添加成功");
		} catch (Exception e)
		{
			e.printStackTrace();
			params.put("error", "插入失败,请刷新重试");
			modelAndView = new ModelAndView("error", params);
		}
		// articleManagementWrappedService.insertOrUpdate(articleDTO);

		// 还需要修改图片的状态 今天先直接增加再说

		return modelAndView;
	}

	/*
	 * users
	 */
	// @RequiresRoles(value= {
	// "admin"
	// })
	@RequestMapping("/users")
	public ModelAndView showAllUsers(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		int pageSize = Integer.parseInt(StringUtils.defaultString(request.getParameter("pageSize"), "5"));
		int pageNum = Integer.parseInt(StringUtils.defaultString(request.getParameter("pageNum"), "1"));
		Map<String, Object> params = new HashMap<>();
		UserDTO user=AdminUtil.getLoginUser();
		params.put("user", user);
		try
		{
			PageDTO<Collection<UserDTO>> pageDTO = userManagementWrappedService.findUsersByPage(pageSize, pageNum,
					params);
			PageVO<Collection<UserDTO>> pageVO = new PageVO<Collection<UserDTO>>(pageDTO.getData(), pageSize, pageNum,
					pageDTO.getTotalCount());
			params.put("pageVO", pageVO);
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("[find users] error {} ", e.getMessage());
			params.put("error", e.getMessage());
			modelAndView = new ModelAndView("error", params);
			return modelAndView;
		}
		modelAndView = new ModelAndView("admin/users", params);
		return modelAndView;
	}

}
