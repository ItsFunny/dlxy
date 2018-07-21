/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月14日 下午6:51:17
* 
*/
package com.dlxy.controller;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.PictureDTO;
import com.dlxy.common.dto.PictureUploadResponseDTO;
import com.dlxy.common.dto.ResultDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRoleDTO;
import com.dlxy.common.enums.ArticlePictureTypeEnum;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.common.enums.ArticleTypeEnum;
import com.dlxy.common.enums.DlxyTitleEnum;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.common.event.AppEvent;
import com.dlxy.common.utils.JsonUtil;
import com.dlxy.common.utils.ResultUtil;
import com.dlxy.common.vo.PageVO;
import com.dlxy.constant.TitleArticleConstant;
import com.dlxy.exception.DlxySystemIllegalException;
import com.dlxy.model.FormArticle;
import com.dlxy.model.FormTitle;
import com.dlxy.model.FormUser;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.server.user.service.IUserRoleService;
import com.dlxy.server.user.service.IUserService;
import com.dlxy.service.IArticleManagementWrappedService;
import com.dlxy.service.IPictureManagementWrappedService;
import com.dlxy.service.ITitleManagementWrappedService;
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
	private IUserService userService;
	@Autowired
	private ITitleService titleService;
	@Autowired
	private IArticleService articleService;
	@Autowired
	private IPictureManagementWrappedService pictureManagementWrappedService;
	@Autowired
	private IUserMangementWrappedService userManagementWrappedService;
	@Autowired
	private AddOrUpdateArtilceCommand addOrUpdateArtilceCommand;
	@Autowired
	private IUserRoleService userRoleService;
	@Autowired
	private ITitleManagementWrappedService titleManagementWrappedService;

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

	@RequestMapping(value = "/article/latest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<Collection<ArticleDTO>> findLatestArticles(HttpServletRequest request,
			HttpServletResponse response)
	{
		/*
		 * 默认为5,可在nosql中配置
		 */
		try
		{
			Collection<ArticleDTO> articles = articleService.findLatestArticleLimited(TitleArticleConstant.MAX_NUMBER_ARTICLES);
			return ResultUtil.sucess(articles);
		} catch (Exception e)
		{
			return ResultUtil.fail();
		}
	}

	@RequestMapping(value = "/article", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<PageVO<Collection<ArticleDTO>>> findNews(HttpServletRequest request, HttpServletResponse response)
	{
		int pageSize = Integer.parseInt(StringUtils.defaultString(request.getParameter("pageSize"), "8"));
		int pageNum = Integer.parseInt(StringUtils.defaultString(request.getParameter("pageNum"), "1"));
		String type = request.getParameter("type");
		Map<String, Object> params = new HashMap<>();
		if (StringUtils.isEmpty(type))
		{
			return ResultUtil.fail("缺失参数");
		} else if (type.equals("news"))
		{
			params.put("articleType", ArticleTypeEnum.INTRODUCE_ARTICLE.ordinal());
		} else if (type.equals("picArticle"))
		{
			params.put("articleType", ArticleTypeEnum.PICTURE_ARTICLE.ordinal());
		} 
		try
		{
			PageDTO<Collection<ArticleDTO>> pageDTO = articleManagementWrappedService.findByParams(pageSize, pageNum,
					params);
			PageVO<Collection<ArticleDTO>> pageVO = new PageVO<Collection<ArticleDTO>>(pageDTO.getData(), pageSize,
					pageNum, pageDTO.getTotalCount());
			return ResultUtil.sucess(pageVO);
		} catch (Exception e)
		{
			logger.error("[find news] error:{}", e.getMessage());
			e.printStackTrace();
		}
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
		Collection<DlxyTitleDTO> collection =  titleService.findChildsByParentId(parentId);
//		if (null == collection || collection.isEmpty())
//		{
//			return ResultUtil.needMoreOp(collection, "无子类目");
//		}
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
	
	@RequestMapping(value="/scrollLoadTitle/{titleId}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void scrollLoadTitles(@PathVariable("titleId")String titleIdStr,HttpServletRequest request,HttpServletResponse response)
	{
		try
		{
			Integer titleId=Integer.parseInt(titleIdStr);
			DlxyTitleDTO dto = titleManagementWrappedService.findChildsAndArticles(titleId, ITitleManagementWrappedService.MAX_SHOW_ARTICLE_NUMBER);
//			String json = JsonUtil.obj2Json(ResultUtil.sucess(dto));
			String json=JSON.toJSONString(ResultUtil.sucess(dto));
			System.out.println(json);
			response.getWriter().write(json);
//			return ResultUtil.sucess(dto);
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("[下拉加载信息]error:{}",e.getMessage());
			try
			{
				response.getWriter().write(JSON.toJSONString(ResultUtil.fail()));
			} catch (IOException e1)
			{
				logger.error("sss");
				e1.printStackTrace();
			}
		}
	}
	
	
	
	/*
	 * 显示新闻相关的类目 这里的数据结构需要重新设置一下
	 */
	// @RequestMapping(value = "/newsTitles", method = RequestMethod.GET, produces =
	// MediaType.APPLICATION_JSON_UTF8_VALUE)
	// public ResultDTO<DlxyTitleDTO> showAllNewsTitles(HttpServletRequest requestq,
	// HttpServletResponse response)
	// {
	// Collection<DlxyTitleDTO> collection =
	// titleService.findTitlesByType(DlxyTitleEnum.NEWS_TITLE.ordinal());
	// DlxyTitleDTO dlxyTitleDTO = null;
	// if (null != collection && !collection.isEmpty())
	// {
	// dlxyTitleDTO = collection.iterator().next();
	// List<DlxyTitleDTO> childs = (List<DlxyTitleDTO>) titleService
	// .findChildsByParentId(dlxyTitleDTO.getTitleId());
	// dlxyTitleDTO.setChildTitles(childs);
	// }
	// return ResultUtil.sucess(dlxyTitleDTO);
	// }

	@RequestMapping(value = "/titles", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<Collection<DlxyTitleDTO>> findAllTitles()
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

	/*
	 * 放到回收站中
	 */
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

	@RequestMapping(value = "/article/update/typeOrStatus", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String> updateArticleStatus(HttpServletRequest request, HttpServletResponse response)
	{
		UserDTO userDTO = AdminUtil.getLoginUser();
		String key = request.getParameter("key");
		String error = "success";
		if (StringUtils.isEmpty(key))
		{
			error = "missing artgument:type";
		}
		String[] articleIds = request.getParameterValues("articleId");
		if (null == articleIds || articleIds.length < 1)
		{
			error = "missing argument:articleId";
		}
		if (error.equals("success"))
		{
			try
			{
				Long[] ids = new Long[articleIds.length];
				for (int i = 0; i < articleIds.length; i++)
				{
					ids[i] = Long.parseLong(articleIds[i]);
				}

				if (key.equals("status"))
				{
					String articleStatusStr = request.getParameter("articleStatus");
					int artilceStatus = 0;
					artilceStatus = Integer.parseInt(articleStatusStr);
					if (artilceStatus < 0 || artilceStatus > 2)
					{
						throw new RuntimeException("文章状态超出范围");
					}
					{
						articleManagementWrappedService.updateArticlesInBatch(userDTO, ids, artilceStatus);
						return ResultUtil.sucess();
					}
				} else if (key.equals("type"))
				{
					String typeStr = request.getParameter("articleType");
					int type = Integer.parseInt(typeStr);
					articleManagementWrappedService.updateArticleTypeInBatch(userDTO, ids, type);
				}
			} catch (Exception e)
			{
				logger.error("[update article status or type ],error:{},key:{}", e.getMessage(), key);
				return ResultUtil.fail(e.getMessage());
			}
			return ResultUtil.sucess();
		} else
		{
			return ResultUtil.fail(error);
		}
	}

	@RequestMapping(value = "/article/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public ResultDTO<String> updateArticle(FormArticle formArticle, BindingResult result, HttpServletRequest request,
			HttpServletResponse response)
	{
		String[] titleIdStrs = request.getParameterValues("titleId");
		Arrays.sort(titleIdStrs, new Comparator<String>()
		{
			@Override
			public int compare(String o1, String o2)
			{
				int i1 = Integer.valueOf(o1);
				int i2 = Integer.valueOf(o2);
				return i1 < i2 ? 1 : -1;
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
//	@RequestMapping(value="/article/update/descpic",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public ResultDTO<String>updateArticleDescPic( @RequestParam Map<String, Object>params,MultipartFile imgFile,HttpServletRequest request,HttpServletResponse response)
//	{
//		String articleIdStr=request.getParameter("articleId");
//		if(StringUtils.isEmpty(articleIdStr) || imgFile==null || imgFile.isEmpty())
//		{
//			return ResultUtil.fail("缺失参数");
//		}
//		try
//		{
//			Long articleId=Long.parseLong(articleIdStr);
//			String url = FileUtil.saveFile(imgFile, articleId, request);
//			PictureDTO pictureDTO=new PictureDTO();
//			pictureDTO.setArticleId(articleId);
//			pictureDTO.setPictureUrl(url);
//			pictureDTO.setPictureType(ArticlePictureTypeEnum.DESCRIPTION_PICTURE.ordinal());
//			pictureManagementWrappedService.addPciture(AdminUtil.getLoginUser(), articleId, new PictureDTO[] {pictureDTO});
//			return ResultUtil.sucess();
//		} catch (Exception e)
//		{
//			logger.error("[update article desc pic] error : {}" ,e.getMessage());
//			return ResultUtil.fail(e.getMessage());
//		}
//		
//	}
	
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
		// pictureDTO.setPictureId(fileName);
		pictureDTO.setPictureUrl(url);
		pictureDTO.setArticleId(Long.parseLong(articleId));
		pictureDTO.setCreateDate(new Date());
		pictureDTO.setPictureStatus(PictureStatusEnum.Invalid.ordinal());
		try
		{
			// 这里上面需要进行设置,使之成为一个集合,至于保存采用多线程的方式
			// pictureService.addPicture(new PictureDTO[] {pictureDTO});
			// pictureService.addPictureWithArticleId(Long.parseLong(articleId), new
			// PictureDTO[] {pictureDTO});
			pictureManagementWrappedService.addPciture(AdminUtil.getLoginUser(), Long.parseLong(articleId),
					new PictureDTO[]
					{ pictureDTO });
			System.out.println(pictureDTO.getPictureId());
			pictureUploadResponseDTO.setError(0);
			pictureUploadResponseDTO.setUrl(url + "?pictureId=" + pictureDTO.getPictureId());
		} catch (SQLException e)
		{
			e.printStackTrace();
			pictureUploadResponseDTO.setError(1);
			pictureUploadResponseDTO.setMessage(e.getMessage());
		}
		return pictureUploadResponseDTO;
	}

	// @RequiresRoles(value= {
	// "admin"
	// })
	@RequestMapping(value = "/user/updateStatus", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String> banUser(HttpServletRequest request, HttpServletResponse response)
	{
		String userIdStr = request.getParameter("userId");
		String statusStr = request.getParameter("status");
		UserDTO userDTO = AdminUtil.getLoginUser();
		if (StringUtils.isEmpty(userIdStr) || StringUtils.isEmpty(statusStr))
		{
			return ResultUtil.fail("错误的参数");
		}
		Long userId = Long.parseLong(userIdStr);
		int status = Integer.parseInt(statusStr);
		try
		{
			userManagementWrappedService.updateUserStatusByUserId(userDTO, userId, status);
			return ResultUtil.sucess();
		} catch (Exception e)
		{
			logger.error("[update user status ] error {}", e.getMessage());
			return ResultUtil.fail(e.getMessage());
		}
	}

	// @RequiresRoles(value= { "admin","subAdmin"
	// })
	@RequestMapping(value = "/user/find", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<UserDTO> findUser(HttpServletRequest request, HttpServletResponse response)
	{
		String keyStr = request.getParameter("q");
		UserDTO userDTO = null;
		try
		{
			Long userId = Long.parseLong(keyStr);
			userDTO = userService.findByUserId(userId);
		} catch (Exception e)
		{
			userDTO = userService.findByUsername(keyStr);
		}
		if (null == userDTO)
		{
			return ResultUtil.fail("no user find");
		} else
		{
			return ResultUtil.sucess(userDTO);
		}
	}

	/*
	 * 列出所有的角色
	 */
	// @RequiresRoles(value= { "admin","subAdmin"
	// })
	@RequestMapping(value = "/user/roles/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<Collection<UserRoleDTO>> findRoles(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			Collection<UserRoleDTO> roles = userRoleService.findAllRoles();
			return ResultUtil.sucess(roles);
		} catch (Exception e)
		{
			logger.error("[find roles] error:{}", e.getMessage());
			return ResultUtil.fail(e.getMessage());
		}
	}

	/*
	 * 添加用户
	 */
	// @RequiresRoles(value= {
	// "admin"
	// })
	// @RequestMapping(value = "/user/doAddUser", consumes =
	// MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces =
	// MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	// public ResultDTO<String> doAddUser(HttpServletRequest request,
	// HttpServletResponse response)
	// {
	// FormUser formUser=new FormUser();
	// Map<String, Object>params=new HashMap<>();
	// try
	// {
	// formUser.valid();
	// } catch (Exception e)
	// {
	// params.put("error", e.getMessage());
	// }
	// if(!params.containsKey("error"))
	// {
	// UserRoleDTO role = userRoleService.findByRoleId(formUser.getRoleId());
	// if(role==null)
	// {
	// params.put("error", "role信息不存在");
	// }else {
	// UserDTO userDTO=new UserDTO();
	// formUser.to(userDTO);
	// try
	// {
	// userManagementWrappedService.addUser(AdminUtil.getLoginUser(), userDTO);
	// } catch (Exception e)
	// {
	// logger.error("[add user] error:{} ",e.getMessage());
	// params.put("error", e.getMessage());
	// }
	// }
	// }
	// if(params.containsKey("error"))
	// {
	// return ResultUtil.fail(params.get("error").toString());
	// }
	// return ResultUtil.sucess();
	// }
	/*
	 * 搜索用户
	 */

}
