package com.dlxy.controller;

import java.io.UnsupportedEncodingException;
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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.dto.UserRoleDTO;
import com.dlxy.common.enums.ArticlePictureTypeEnum;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.common.enums.ArticleTypeEnum;
import com.dlxy.common.enums.DlxyTitleEnum;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.common.service.IdWorkerService;
import com.dlxy.common.utils.PageResultUtil;
import com.dlxy.common.vo.PageVO;
import com.dlxy.exception.DlxySystemIllegalException;
import com.dlxy.model.FormArticle;
import com.dlxy.model.FormUser;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.server.user.model.DlxyUser;
import com.dlxy.server.user.service.IUserRoleService;
import com.dlxy.server.user.service.IUserService;
import com.dlxy.service.IArticleWrappedService;
import com.dlxy.service.IPictureWrappedService;
import com.dlxy.service.IUserWrappedService;
import com.dlxy.service.command.AddOrUpdateArtilceCommand;
import com.dlxy.shiro.DlxyShiroAuthToken;
import com.dlxy.utils.AdminUtil;
import com.dlxy.utils.FileUtil;
import com.joker.library.utils.CommonUtils;
import com.joker.library.utils.KeyUtils;

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
	Pattern passwrodPattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$");
	Pattern phonePattern = Pattern.compile("^(1[34578])\\d{9}$");
	private Logger logger = LoggerFactory.getLogger(AdminController.class);
	@Autowired
	private ITitleService titleService;
	@Autowired
	private IArticleWrappedService articleManagementWrappedService;
	@Autowired
	private AddOrUpdateArtilceCommand articleCommand;
	@Autowired
	private IUserWrappedService userManagementWrappedService;
	@Autowired
	private IPictureWrappedService pictureManagementWrappedService;

	@Autowired
	private IdWorkerService idWorkService;
	@Autowired
	private IUserRoleService userRoleService;

	@Autowired
	private IUserService userService;

	@RequestMapping("/login")
	public ModelAndView test(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		UserDTO user = AdminUtil.getLoginUser();
		if (null == user)
		{
			modelAndView = new ModelAndView("login");
		} else
		{
			modelAndView = new ModelAndView("admin/index");
		}
		return modelAndView;
	}

	@RequestMapping("/doLogin")
	public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		Map<String, Object> params = new HashMap<>();
		String realname = request.getParameter("realname");
		String passwrod = request.getParameter("password");
		String vcode = request.getParameter("vcode");
		String kcode = (String) request.getSession(true).getAttribute("kcode");
		if (StringUtils.isEmpty(vcode) || !vcode.equals(kcode))
		{
			params.put("error", "验证码不正确");
			modelAndView = new ModelAndView("login", params);
			return modelAndView;
		}
		UserDTO dbUser = userService.findByUsername(realname);

		if (null == dbUser || !dbUser.getPassword().equals(KeyUtils.md5Encrypt(passwrod)))
		{
			params.put("error", "用户不存在,或者密码错误,忘记密码请联系管理员");
			modelAndView = new ModelAndView("login", params);
		} else if (!dbUser.isAble())
		{
			params.put("error", "此用户已被禁止登录");
			modelAndView = new ModelAndView("login", params);
		} else
		{
			SecurityUtils.getSubject().login(new DlxyShiroAuthToken(dbUser, dbUser.getPassword()));

			DlxyUser dlxyUser = new DlxyUser();
			dlxyUser.setUserId(dbUser.getUserId());
			dlxyUser.setLastLoginDate(new Date());
			dlxyUser.setLastLoginIp(CommonUtils.getRemortIP(request));
			userService.updateUserByExample(dlxyUser);

			if (null == dbUser.getLastLoginDate())
			{
				params.put("error", "第一次登录,强烈建议您修改用户密码");
				params.put("user", dbUser);
				modelAndView = new ModelAndView("redirect:/admin/user/userInfo/update.html", params);
			} else
			{
				modelAndView = new ModelAndView("redirect:/admin/index.html");
			}
			dlxyUser = null;
		}
		return modelAndView;
	}

	public static void main(String[] args)
	{
		String string = "202cb962ac59075b964b07152d234b70";
		String string2 = "202cb962ac59075b964b07152d234b70";
		System.out.println(string.equals(string2));
	}

	@RequestMapping("/logout")
	public ModelAndView LogoutAware(HttpServletRequest request, HttpServletResponse response)
	{
		SecurityUtils.getSubject().logout();
		ModelAndView modelAndView = new ModelAndView("login");
		return modelAndView;
	}

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response)
	{
		UserDTO user = AdminUtil.getLoginUser();
		ModelAndView modelAndView = new ModelAndView("admin/index");
		modelAndView.addObject("user", user);
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
		ModelAndView modelAndView = new ModelAndView("admin/news_titles");
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
			/*
			 * 查询类目下的所有文章 1.类目是父类目 1):遍历得到所有的子类,然后查询 2.类目是子类目 1):则直接查询该类目下的文章
			 * 
			 */
			// DlxyTitleDTO titleDTO = titleService.findById(titleId);
			// if(titleDTO.getTitleParentId()==0)
			// {
			//
			// }
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
			IllegalLogDTO illegalLogDTO = new IllegalLogDTO(CommonUtils.getIpAddr(request), user.getUserId(),
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
		UserDTO user = AdminUtil.getLoginUser();
		List<Long>pictureIdList=new ArrayList<>();
		String[] pictureIds = request.getParameterValues("pictureId");
		String url = null;
		PictureDTO descPic = null;
		Map<String, Object> params = new HashMap<>();
		if (bindingResult.hasErrors())
		{
			String error = "";
			for (ObjectError objectError : bindingResult.getAllErrors())
			{
				error += objectError.getDefaultMessage();
			}
			params.put("error", error);
			modelAndView = new ModelAndView("error", params);
			return modelAndView;
		}
		
		if(null!=pictureIds && pictureIds.length>0)
		{
			for (String string : pictureIds)
			{
				pictureIdList.add(Long.parseLong(string));
			}
		}
	
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
				pictureIdList.add(descPic.getPictureId());
//				if (null == pictureIds || pictureIds.length <= 0)
//				{
//					pictureIds = new String[1];
//					pictureIds[0] = descPic.getPictureId().toString();
//				} else
//				{
//					pictureIds = new String[pictureIds.length + 1];
//					pictureIds[pictureIds.length - 1] = descPic.getPictureId().toString();
//				}
			} catch (Exception e1)
			{
				e1.printStackTrace();
				params.put("error", e1.getMessage());
			}
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

		articleDTO.setUserId(user.getUserId());
		articleDTO.setRealname(user.getRealname());
//		articleDTO.setPictureIds(pictureIds);
		// PictureDTO pictureDTO=new PictureDTO();
		// pictureDTO.setArticleId(articleDTO.getArticleId());
		// pictureDTO.setPictureStatus(PictureStatusEnum.Effective.ordinal());
		params.put("articleDTO", articleDTO);
		// params.put("pictureDTO", pictureDTO);
		if(pictureIdList.size()>0)
		{
			params.put("pictureStatus", PictureStatusEnum.Effective.ordinal());
			params.put("pictureIdList", pictureIdList);
		}
		
		try
		{
			articleCommand.execute(params);
			modelAndView = new ModelAndView("redirect:/admin/articles.html?type=all");
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
	public ModelAndView updateDsescPic(@RequestParam("imgFile") MultipartFile imgFile, HttpServletRequest request,
			HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		Map<String, Object> params = new HashMap<>();
		String articleIdStr = request.getParameter("articleId");
		if (StringUtils.isEmpty(articleIdStr) || imgFile == null || imgFile.isEmpty())
		{
			params.put("error", "缺失参数");
		}
		if (params.containsKey("error"))
		{
			return new ModelAndView("error", params);
		}
		try
		{
			Long articleId = Long.parseLong(articleIdStr);
			String url = FileUtil.saveFile(imgFile, articleId, request);
			PictureDTO pictureDTO = new PictureDTO();
			pictureDTO.setArticleId(articleId);
			pictureDTO.setPictureUrl(url);
			pictureDTO.setPictureStatus(PictureStatusEnum.Effective.ordinal());
			pictureDTO.setPictureType(ArticlePictureTypeEnum.DESCRIPTION_PICTURE.ordinal());
			pictureManagementWrappedService.updateDescPicture(AdminUtil.getLoginUser(), articleId, pictureDTO);

		} catch (Exception e)
		{
			logger.error("[update article desc pic] error : {}", e.getMessage());
			params.put("error", e.getMessage());
		}
		if (params.containsKey("error"))
		{
			modelAndView = new ModelAndView("error", params);
		} else
		{
			modelAndView = new ModelAndView("redirect:/admin/articles.html?type=picArticles", params);
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

	@RequestMapping("/user/records")
	public ModelAndView showUserRecord(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") String pageSizeStr,
			@RequestParam(name = "pageNum", required = false, defaultValue = "1") String pageNumStr)
	{
		ModelAndView modelAndView = null;
		UserDTO user = AdminUtil.getLoginUser();
		Long cUserId = user.getUserId();
		Map<String, Object> params = new HashMap<>();
		String userIdStr = request.getParameter("userId");
		String q = request.getParameter("q");

		Integer pageSize = Integer.parseInt(pageSizeStr);
		Integer pageNum = Integer.parseInt(pageNumStr);
		if (!StringUtils.isEmpty(q))
		{
			try
			{
				q = new String(q.getBytes("iso-8859-1"), "utf-8");
				cUserId = Long.parseLong(q);
				params.put("userId", cUserId);
			} catch (NumberFormatException e)
			{
				params.put("q", q);
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
				params.put("error", "查询参数错误,请输入正确的值");
				return new ModelAndView("error", params);
			}
		} else if (!StringUtils.isEmpty(userIdStr))
		{
			cUserId = Long.parseLong(userIdStr);
			if (!user.isAdmin() && !user.getUserId().equals(cUserId))
			{
				IllegalLogDTO illegalLogDTO = new IllegalLogDTO(CommonUtils.getRemortIP(request), user.getUserId(),
						"试图跨权访问他人操作记录", IllegalLevelEnum.Suspicious.ordinal());
				throw new DlxySystemIllegalException("试图跨全访问他人操作记录", illegalLogDTO);
			}
			params.put("userId", cUserId);
		} else
		{
			if (!user.isAdmin())
			{
				params.put("userId", cUserId);
			}
		}
		try
		{
			PageDTO<Collection<UserRecordDTO>> pageDTO = userManagementWrappedService.findUserRecords(pageSize, pageNum,
					params);
			PageVO<Collection<UserRecordDTO>> pageVO = new PageVO<Collection<UserRecordDTO>>(pageDTO.getData(),
					pageSize, pageNum, pageDTO.getTotalCount());
			params.put("pageVO", pageVO);
			params.put("user", user);
			modelAndView = new ModelAndView("admin/records", params);
		} catch (Exception e)
		{
			logger.error("[查询用户操作记录]error:{},cause:{}", e.getMessage(), e.getCause());
			e.printStackTrace();
			modelAndView = new ModelAndView("error");
			modelAndView.addObject("error", e.getMessage());
		}
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
	public ModelAndView doAddUser(@Valid FormUser formUser, BindingResult result, HttpServletRequest request,
			HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		UserDTO user = AdminUtil.getLoginUser();
		Map<String, Object> params = new HashMap<>();
		params.put("user", AdminUtil.getLoginUser());
		if (result.hasErrors())
		{
			String e = "";
			for (ObjectError error : result.getAllErrors())
			{
				e += error.getDefaultMessage();
			}
			params.put("error", e);
		} else if (!realNamePattern.matcher(formUser.getRealname()).matches()
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

	@RequestMapping("/user/userInfo/update")
	public ModelAndView updateUserInfo(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("admin/update_userinfo", params);
		if (params.containsKey("error"))
		{
			try
			{
				params.put("error", new String(params.get("error").toString().getBytes("iso-8859-1"), "utf-8"));
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
				logger.error("[更新用户信息] error:{}", e.getMessage());
				params.remove("error");
			}
		}
		modelAndView.addObject("user", AdminUtil.getLoginUser());
		return modelAndView;
	}

	@RequestMapping("/user/userInfo/doUpdate")
	public ModelAndView doUpdateUserInfo(DlxyUser user, HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		UserDTO loginUser = AdminUtil.getLoginUser();
		Map<String, Object> params = new HashMap<>();
		if (null == user.getUserId() || user.getUserId() < 0)
		{
			params.put("error", "用户id不存在");
		} else if (!user.getUserId().equals(loginUser.getUserId()) && !loginUser.isAdmin())
		{
			// params.put("error", "无权修改他人信息");
			logger.error("[更新用户信息]用户:{} 试图修改他人信息", loginUser.getRealname());
			IllegalLogDTO illegalLogDTO = new IllegalLogDTO(CommonUtils.getRemortIP(request), loginUser.getUserId(),
					"试图修改他人信息", IllegalLevelEnum.Serious.ordinal());
			throw new DlxySystemIllegalException(illegalLogDTO);
		}

		params.put("user", loginUser);
		// if(!passwrodPattern.matcher(user.getPassword()).matches())
		// {
		// params.put("error", "密码格式不正确,密码长度为6-16位,且包含字母和数字");
		// }else
		// if(!realNamePattern.matcher(user.getRealname()).matches())
		// {
		// params.put("error", "真实姓名格式不正确,王大二或者Justain Bieber 格式");
		// }
		// else if(!phonePattern.matcher(mobilePhone).matches())
		// {
		// params.put("error", "手机格式不正确");
		// }
		if (params.containsKey("error"))
		{
			modelAndView = new ModelAndView("admin/update_userinfo", params);
			return modelAndView;
		}
		try
		{
			if (!loginUser.getRealname().equals(user.getRealname()))
			{
				UserDTO dbUser = userService.findByUsername(user.getRealname());
				if (dbUser != null)
				{
					params.put("error", "用户名冲突,重新输入用户名");
					modelAndView = new ModelAndView("admin/update_userinfo", params);
					return modelAndView;
				}
			}
			user.setPassword(KeyUtils.md5Encrypt(user.getPassword()));
			int count = userService.updateUserByUserId(user);
			if (count > 0)
			{
				modelAndView = new ModelAndView("redirect:/admin/index.html");
			} else
			{
				params.put("error", "发生未知错误,请尝试重新更新,若再次失败请联系管理员");
				modelAndView = new ModelAndView("admin/update_userinfo", params);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("[更新用户信息] error:{} cause:{}", e.getMessage(), e.getCause());
			params.put("error", e.getMessage());
			modelAndView = new ModelAndView("admin/update_userinfo", params);
		}
		return modelAndView;
	}

	@RequiresRoles(value =
	{ "admin" })
	@RequestMapping("/user/search")
	public ModelAndView searchUser(@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum, HttpServletRequest request,
			HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		String q = request.getParameter("q");
		Map<String, Object> params = new HashMap<>();
		params.put("user", AdminUtil.getLoginUser());
		if (StringUtils.isEmpty(q))
		{
			params.put("error", "查询参数不能为空");
		} else
		{
			PageDTO<Collection<UserDTO>> pageDTO = null;
			try
			{
				Long userId = Long.parseLong(q);
				UserDTO userDTO = userService.findByUserId(userId);
				pageDTO = new PageDTO<Collection<UserDTO>>(1L, Arrays.asList(userDTO));
			} catch (NumberFormatException e)
			{
				params.put("realname", q);
				try
				{
					pageDTO = userManagementWrappedService.findUsersByPage(pageSize, pageNum, params);
				} catch (SQLException e1)
				{
					e1.printStackTrace();
					logger.error("[查询用户]sql error {},{}", e.getMessage(), e.getCause());
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				logger.error("[查询用户]error {},{}", e.getMessage(), e.getCause());
				pageDTO = PageResultUtil.emptyPage();
			}
			PageVO<Collection<UserDTO>> pageVO = new PageVO<Collection<UserDTO>>(pageDTO.getData(), pageSize, pageNum,
					pageDTO.getTotalCount());
			params.put("pageVO", pageVO);
			// try
			// {
			// user = userService.findUserByNameOrId(q);
			// params.put("user", user);
			// } catch (SQLException e)
			// {
			// e.printStackTrace();
			// logger.error("[查询用户] error {},{}",e.getMessage(),e.getCause());
			// }

		}
		modelAndView = new ModelAndView("admin/users", params);
		return modelAndView;
	}
}
