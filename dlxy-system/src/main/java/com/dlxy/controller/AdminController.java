/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月14日 下午6:51:43
* 
*/
package com.dlxy.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.PictureDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRoleDTO;
import com.dlxy.common.enums.ArticlePictureTypeEnum;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.common.enums.ArticleTypeEnum;
import com.dlxy.common.enums.DlxyTitleEnum;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.common.service.IdWorkerService;
import com.dlxy.common.utils.ResultUtil;
import com.dlxy.common.vo.PageVO;
import com.dlxy.exception.DlxySystemIllegalException;
import com.dlxy.model.FormArticle;
import com.dlxy.model.FormUser;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.server.user.service.IUserRoleService;
import com.dlxy.service.IArticleManagementWrappedService;
import com.dlxy.service.IPictureManagementWrappedService;
import com.dlxy.service.IUserMangementWrappedService;
import com.dlxy.service.command.AddOrUpdateArtilceCommand;
import com.dlxy.utils.AdminUtil;
import com.dlxy.utils.FileUtil;
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
	Pattern realNamePattern = Pattern.compile("^([\u4e00-\u9fa5]{1,20}|[a-zA-Z]+ [a-zA-Z]+)$");
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
	private IPictureManagementWrappedService pictureManagementWrappedService;

	@Autowired
	private IdWorkerService idWorkService;
	@Autowired
	private IUserRoleService userRoleService;

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
		modelAndView.addObject("admin/user", user);
		return modelAndView;
	}

	/*
	 * 显示学院相关的类目
	 */
	@RequestMapping("/aboutTitles")
	public ModelAndView showAllTiltles(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("admin/about_titles");
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
			modelAndView.addObject("admin/titles", childs);
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
			/*
			 * 查询类目下的所有文章
			 * 1.类目是父类目
			 * 1):遍历得到所有的子类,然后查询
			 * 2.类目是子类目
			 * 1):则直接查询该类目下的文章
			 * 	
			 */
//			DlxyTitleDTO titleDTO = titleService.findById(titleId);
//			if(titleDTO.getTitleParentId()==0)
//			{
//				
//			}
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
			modelAndView = new ModelAndView("admin/title_article_detail", params);
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
		int pageSize = Integer.parseInt(StringUtils.defaultString(request.getParameter("pageSize"), "10"));
		int pageNum = Integer.parseInt(StringUtils.defaultString(request.getParameter("pageNum"), "1"));
		String userIdStr = request.getParameter("userId");
		String type = StringUtils.defaultString(request.getParameter("type"), "");
		if (type.equals("picArticles"))
		{
			params.put("articleType", ArticleTypeEnum.PICTURE_ARTICLE.ordinal());
		} else if (type.equals("news"))
		{
			params.put("articleType", ArticleTypeEnum.INTRODUCE_ARTICLE.ordinal());
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
			modelAndView = new ModelAndView("admin/articles", params);
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
		ModelAndView modelAndView = new ModelAndView("admin/article_add", params);
		return modelAndView;
	}

	@RequestMapping("/article/doAddArticle")
	public ModelAndView doAddArticle(@Valid FormArticle formArticle, BindingResult bindingResult,
			@RequestParam(name = "imgFile", required = false) MultipartFile imgFile, HttpServletRequest request,
			HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		String[] pictureIds = request.getParameterValues("pictureId");
		String url = null;
		PictureDTO descPic = null;
		// 需要事务
		if (null != imgFile && !imgFile.isEmpty())
		{
			url = FileUtil.saveFile(imgFile, formArticle.getArticleId(), request);
			descPic = new PictureDTO();
			descPic.setPictureUrl(url);
			descPic.setArticleId(formArticle.getArticleId());
			descPic.setCreateDate(new Date());
			descPic.setPictureType(ArticlePictureTypeEnum.DESCRIPTION_PICTURE.ordinal());
			descPic.setPictureStatus(PictureStatusEnum.Invalid.ordinal());
			try
			{
				pictureManagementWrappedService.addPciture(AdminUtil.getLoginUser(), formArticle.getArticleId(),
						new PictureDTO[]
						{ descPic });
				if (null == pictureIds || pictureIds.length <= 0)
				{
					pictureIds = new String[1];
					pictureIds[0] = descPic.getPictureId().toString();
				}else {
					pictureIds=new String[pictureIds.length+1];
					pictureIds[pictureIds.length-1]=descPic.getPictureId().toString();
				}
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
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
	@RequestMapping("/article/update/descpic")
	public ModelAndView updateDsescPic(@RequestParam("imgFile") MultipartFile imgFile,HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView=null;
		Map<String, Object>params=new HashMap<>();
		String articleIdStr=request.getParameter("articleId");
		if(StringUtils.isEmpty(articleIdStr) || imgFile==null || imgFile.isEmpty())
		{
			params.put("error", "缺失参数");
		}
		if(params.containsKey("error"))
		{
			return new ModelAndView("error",params);
		}
		try
		{
			Long articleId=Long.parseLong(articleIdStr);
			String url = FileUtil.saveFile(imgFile, articleId, request);
			PictureDTO pictureDTO=new PictureDTO();
			pictureDTO.setArticleId(articleId);
			pictureDTO.setPictureUrl(url);
			pictureDTO.setPictureStatus(PictureStatusEnum.Effective.ordinal());
			pictureDTO.setPictureType(ArticlePictureTypeEnum.DESCRIPTION_PICTURE.ordinal());
			pictureManagementWrappedService.updateDescPicture(AdminUtil.getLoginUser(), articleId, pictureDTO);
			
		} catch (Exception e)
		{
			logger.error("[update article desc pic] error : {}" ,e.getMessage());
			params.put("error", e.getMessage());
		}
		if(params.containsKey("error"))
		{
			modelAndView=new ModelAndView("error",params);
		}else {
			modelAndView=new ModelAndView("redirect:/admin/articles.html?type=picArticles",params);
		}
		return modelAndView;
	}

	/*
	 * users
	 */
	// @RequiresRoles(value= {
	// "admin"
	// })
	@RequestMapping("/users")
	public ModelAndView showAllUsers(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		int pageSize = Integer.parseInt(StringUtils.defaultString(request.getParameter("pageSize"), "5"));
		int pageNum = Integer.parseInt(StringUtils.defaultString(request.getParameter("pageNum"), "1"));
		UserDTO user = AdminUtil.getLoginUser();
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

	// @RequiresRoles(value =
	// { "admin" })
	@RequestMapping("/user/add")
	public ModelAndView addUser(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("admin/user_add");
		modelAndView.addObject("user", AdminUtil.getLoginUser());
		return modelAndView;
	}

	@RequestMapping("/user/doAddUser")
	public ModelAndView doAddUser(FormUser formUser, HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		UserDTO user = AdminUtil.getLoginUser();
		Map<String, Object> params = new HashMap<>();
		params.put("user", AdminUtil.getLoginUser());
		if (!realNamePattern.matcher(formUser.getRealname()).matches()
				|| !CommonUtils.validString(formUser.getRealname()))
		{
			params.put("error", "姓名格式不正确,正确格式:吕小聪 或者Justin Bieber,请不要包含特殊字符<>? 等");
		}
		if (!params.containsKey("error"))
		{
			UserRoleDTO userRole = userRoleService.findByRoleId(formUser.getRoleId());
			if (null == userRole)
			{
				params.put("error", "无效的角色信息,请刷新重试");
			} else
			{

				UserDTO userDTO = new UserDTO();
				formUser.to(userDTO);
				try
				{
					userManagementWrappedService.addUser(user, userDTO);
				} catch (Exception e)
				{
					logger.error("[add user] error:{}", e.getMessage());
					params.put("error", "用户已存在,请更换名字");
				}
			}
		}
		if (params.containsKey("error"))
		{
			modelAndView = new ModelAndView("admin/user_add", params);
		} else
		{
			params.put("error", "添加成功");
			modelAndView = new ModelAndView("redirect:/admin/users.html", params);
		}
		return modelAndView;
	}
}
