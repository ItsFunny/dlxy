package com.dlxy.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.PictureDTO;
import com.dlxy.common.dto.PictureUploadResponseDTO;
import com.dlxy.common.dto.ResultDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRoleDTO;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.common.enums.ArticleTypeEnum;
import com.dlxy.common.enums.DlxyTitleEnum;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.common.utils.RSAUtils;
import com.dlxy.common.utils.ResultUtil;
import com.dlxy.common.vo.PageVO;
import com.dlxy.config.DlxyProperty;
import com.dlxy.constant.TitleArticleConstant;
import com.dlxy.exception.DlxySystemIllegalException;
import com.dlxy.listener.ShiroSessionListener;
import com.dlxy.listener.UserVisitListener;
import com.dlxy.model.FormArticle;
import com.dlxy.model.FormTitle;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.server.user.model.DlxyLink;
import com.dlxy.server.user.model.DlxyUser;
import com.dlxy.server.user.model.DlxyVisitRecord;
import com.dlxy.server.user.service.ILinkService;
import com.dlxy.server.user.service.IUserRoleService;
import com.dlxy.server.user.service.IUserService;
import com.dlxy.server.user.service.IVisitRecordService;
import com.dlxy.service.IArticleWrappedService;
import com.dlxy.service.IPictureWrappedService;
import com.dlxy.service.IRedisService;
import com.dlxy.service.ITitleWrappedService;
import com.dlxy.service.IUserWrappedService;
import com.dlxy.service.command.AddOrUpdateArtilceCommand;
import com.dlxy.service.command.DeleteArticleCommand;
import com.dlxy.utils.AdminUtil;
import com.joker.library.file.FileStrategyContext;
import com.joker.library.file.IFileStrategy;
import com.joker.library.utils.CommonUtils;
import com.joker.library.utils.DateUtils;

@RestController
@RequestMapping("/api/v1")
public class RestAPIController
{
	// @Autowired
	// private ITitleManagementWrappedService titleManagementWrappedServiceImpl;

	private Logger logger = LoggerFactory.getLogger(RestAPIController.class);
	@Autowired
	private IArticleWrappedService articleManagementWrappedService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ITitleService titleService;
	@Autowired
	private IArticleService articleService;
	@Autowired
	private IPictureWrappedService pictureManagementWrappedService;
	@Autowired
	private IUserWrappedService userManagementWrappedService;
	@Autowired
	private AddOrUpdateArtilceCommand addOrUpdateArtilceCommand;
	@Autowired
	private IUserRoleService userRoleService;
	@Autowired
	private ITitleWrappedService titleManagementWrappedService;
	@Autowired
	private DlxyProperty dlxyProperty;
	@Autowired
	private DeleteArticleCommand deleteArticleCommand;

	@Autowired
	private FileStrategyContext fileService;
	@Autowired
	private ILinkService linkService;
	@Autowired
	private IRedisService redisService;

	@Autowired
	private IVisitRecordService visitRecordService;
	@RequestMapping(value = "/address/images", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String> getImagesAddress(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException
	{
		String token = request.getParameter("token");
		token = URLDecoder.decode(token, "utf-8");
		if (StringUtils.isEmpty(token))
		{
			return ResultUtil.fail("缺少参数token");
		}
		try
		{
			RSAUtils.decryptByPrivate(token, dlxyProperty.getPrivateKeyBytes());
			String path = request.getServletContext().getRealPath("");
			return ResultUtil.sucess(path, "sucess");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();

			// IllegalLogDTO illegalLogDTO=new
			// IllegalLogDTO(CommonUtils.getRemortIP(request), userid, detail, level)
		} catch (Exception e)
		{
			logger.error("[解析token出错],error :{}", e.getMessage());
			return ResultUtil.fail("解析错误" + e.getMessage());
		}
		return ResultUtil.fail();
	}

	@RequestMapping(value = "/article/visitCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<Integer> getArticleVisitCount(HttpServletRequest request, HttpServletResponse response)
	{
		String articleIdStr = request.getParameter("articleId");
		String ip = CommonUtils.getRemortIP(request);
		Integer res = 0;
		Long articleId = Long.parseLong(articleIdStr);
		res = articleManagementWrappedService.findArticleVisitCount(articleId, ip);
//		最好还是采用原先的方式,因为需要对ip和时间进行判断,那种做法是最好的,就是只有1个缺点,factroy这个类内存占用会逐渐增大
//		String json = redisService.get(String.format(IRedisService.ARTICLE_VISIT_COUNT, articleId));
//		if(StringUtils.isEmpty(json))
//		{
//			ArticleDTO articleDTO = articleService.findByArticleId(articleId);
//			ArticleVisitInfo articleVisitInfo=new ArticleVisitInfo();
//			articleVisitInfo.setArticleId(articleId);
//			articleVisitInfo.setArticleName(articleDTO.getArticleName());
//			articleVisitInfo.getVisitors().put(ip, System.currentTimeMillis());
//			articleVisitInfo.incr();
//			.............
//		}
		return ResultUtil.sucess(res);
	}
	@RequestMapping(value="/visitCount",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String>getTotalCountAndPerDayCount(HttpServletRequest request,HttpServletResponse response)
	{
		String totalVisitCount="";
		String perDayVisitCount="";
		Long currentDate=DateUtils.getCurrentDay();
		try
		{
			totalVisitCount=redisService.get(IRedisService.WEB_VISIT_TOTAL_COUT);
			perDayVisitCount=redisService.get(String.format(IRedisService.PER_DAY_VISIT_COUNT, currentDate));
		} catch (Exception e)
		{
			logger.error("[Redis]服务器挂了");
			totalVisitCount=visitRecordService.findByType(IVisitRecordService.TOTAL).iterator().next().getVisitCount().toString();
			DlxyVisitRecord findByRecordDate = visitRecordService.findByRecordDate(currentDate);
			if(null==findByRecordDate)
			{
				perDayVisitCount="0";
				DlxyVisitRecord record=new DlxyVisitRecord();
				record.setVisitCount(0);
				record.setVisitRecordType(IVisitRecordService.PER_DAY);
				visitRecordService.addOrUpdate(record);
			}else {
				perDayVisitCount=findByRecordDate.getVisitCount().toString();
			}
		}
		return ResultUtil.sucess(totalVisitCount+","+perDayVisitCount+","+UserVisitListener.onlineCount.get(),"success");
	}

	@RequestMapping(value = "/link/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String> deleteLinkByLinkId(HttpServletRequest request, HttpServletResponse response)
	{
		String linkIdStr = request.getParameter("linkId");
		UserDTO user = AdminUtil.getLoginUser();
		if (StringUtils.isEmpty(linkIdStr))
		{
			return ResultUtil.fail("缺失参数linkId");
		}
		DlxyLink link = linkService.findById(Integer.parseInt(linkIdStr));
		if (null == link)
		{
			IllegalLogDTO illegalLogDTO = new IllegalLogDTO(CommonUtils.getIpAddr(request), user.getUserId(),
					"试图删除不存在的超链接", IllegalLevelEnum.Suspicious.ordinal());
			throw new DlxySystemIllegalException(illegalLogDTO);
		}
		userManagementWrappedService.deleteLinkByLinkId(user, link);
		return ResultUtil.sucess();
	}

	@RequestMapping(value = "/admin/title/addOrUpdate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String> addOrUpdateTitle(@Valid FormTitle formTitle, BindingResult result,
			HttpServletRequest request, HttpServletResponse response)
	{
		String error = "";
		if (result.hasErrors())
		{
			for (ObjectError error2 : result.getAllErrors())
			{
				error += error2.getDefaultMessage();
			}
		}
		if (!CommonUtils.validString(formTitle.getTitleName()))
		{
			error = "类型名称不合法,请重新输入,不能有<>!等";
		} else if (formTitle.getTitleDisplaySeq() > 100 || formTitle.getTitleDisplaySeq() < 0)
		{
			error = "排序号范围为0-100";
		} else if (CommonUtils.isContainsChinese(formTitle.getTitleAbbName()))
		{
			error = "缩写名称不能包含中文";
		}
		if (!StringUtils.isEmpty(error))
		{
			return ResultUtil.fail(error);
		}
		DlxyTitleDTO titleDTO = new DlxyTitleDTO();
		formTitle.to(titleDTO);
		UserDTO userDTO = AdminUtil.getLoginUser();
		try
		{
			DlxyTitleDTO dbTitle = titleService.findByAbbName(formTitle.getTitleAbbName());
			if (null == titleDTO.getTitleId())
			{
				if (null != dbTitle)
				{
					return ResultUtil.fail("缩写名字重复了,请重新输入,尽量以拼音缩写或全英文代替");
				}
			} else
			{
				if (null != dbTitle && !dbTitle.getTitleId().equals(titleDTO.getTitleId()))
				{
					return ResultUtil.fail("缩写名字重复了,请重新输入,尽量以拼音缩写或全英文代替");
				}
			}
			articleManagementWrappedService.addTitleOrUpdate(userDTO, titleDTO);
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("[addOrUpdate title ] occur error {}", e.getMessage());
			return ResultUtil.fail(e.getMessage());
		}
		return ResultUtil.sucess();
	}

	/*
	 * limited condition: username,articleId,articleName,titleId searchCondition:
	 * userName,articleName
	 */
	@RequestMapping("/admin/search/article")
	public ResultDTO<PageVO<Collection<ArticleDTO>>> searchArticle(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		Map<String, Object> params = new HashMap<>();
		UserDTO user = AdminUtil.getLoginUser();
		String q = request.getParameter("q");
		String pageSizeStr = StringUtils.defaultString(request.getParameter("pageSize"), "2");
		String pageNumStr = StringUtils.defaultString(request.getParameter("pageNum"), "1");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		Integer pageSize = Integer.parseInt(pageSizeStr);
		Integer pageNum = Integer.parseInt(pageNumStr);
		if (!StringUtils.isEmpty(startTime))
		{
			params.put("startTime", startTime);
		}
		if (!StringUtils.isEmpty(endTime))
		{
			params.put("endTime", endTime);
		}
		if (!StringUtils.isEmpty(q))
		{
			q = new String(q.getBytes("iso-8859-1"), "utf-8");
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
						"试图跨权访问" + userIdStr + "记录", IllegalLevelEnum.Suspicious.ordinal());
				response.getWriter().write("sorry u dont have the permission");
				throw new DlxySystemIllegalException(illegalLogDTO);
			}
			params.put("userId", userIdStr);
		}
		try
		{
			Long articleId = Long.parseLong(searchQuery);
			params.put("articleId", articleId);
		} catch (NumberFormatException e)
		{
			params.put("searchParam", searchQuery);
		}
		// catch (SQLException e)
		// {
		// logger.error("[rest api :search 查询文章] find articleById occur sql exception");
		// e.printStackTrace();
		// return ResultUtil.fail(e.getMessage());
		// }
		try
		{
			PageDTO<Collection<ArticleDTO>> pageRes = articleManagementWrappedService.findByParams(pageSize, pageNum,
					params);
			return ResultUtil.sucess(
					new PageVO<Collection<ArticleDTO>>(pageRes.getData(), pageSize, pageNum, pageRes.getTotalCount()),
					"sucess");
		} catch (SQLException e1)
		{
			logger.error("[rest api :search 查询文章] find articleByParam occur sql exception");
			e1.printStackTrace();
			return ResultUtil.fail(e1.getMessage());
		}
	}

	@RequestMapping(value = "/links", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<Collection<DlxyLink>> findAlllinks(HttpServletRequest request, HttpServletResponse response)
	{
		List<DlxyLink> links = linkService.findAllLinks();
		return ResultUtil.sucess(links);
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
			Collection<ArticleDTO> articles = articleService
					.findLatestArticleLimited(TitleArticleConstant.MAX_NUMBER_ARTICLES);
			return ResultUtil.sucess(articles);
		} catch (Exception e)
		{
			return ResultUtil.fail();
		}
	}

	@RequestMapping(value = "/articles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
			params.put("articleStatus", ArticleStatusEnum.UP.ordinal());
		} else if (type.equals("picArticle"))
		{
			params.put("articleType", ArticleTypeEnum.PICTURE_ARTICLE.ordinal());
			params.put("articleStatus", ArticleStatusEnum.UP.ordinal());
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
		Collection<DlxyTitleDTO> collection = titleService.findChildsByParentId(parentId);
		// if (null == collection || collection.isEmpty())
		// {
		// return ResultUtil.needMoreOp(collection, "无子类目");
		// }
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

	@RequestMapping(value = "/scrollLoadTitle/{titleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void scrollLoadTitles(@PathVariable("titleId") String titleIdStr, HttpServletRequest request,
			HttpServletResponse response)
	{
		try
		{
			Integer titleId = Integer.parseInt(titleIdStr);
			DlxyTitleDTO dto = titleManagementWrappedService.findChildsAndArticles(titleId,
					ITitleWrappedService.MAX_SHOW_ARTICLE_NUMBER);
			// String json = JsonUtil.obj2Json(ResultUtil.sucess(dto));
			SerializeConfig serializeConfig = new SerializeConfig();
			serializeConfig.put(Long.class, ToStringSerializer.instance);
			String json = JSON.toJSONString(ResultUtil.sucess(dto), serializeConfig);
			response.getWriter().write(json);
			// return ResultUtil.sucess(dto);
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("[下拉加载信息]error:{}", e.getMessage());
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

	@RequestMapping(value = "/admin/title/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
			boolean res = articleManagementWrappedService.deleteTitleAndUpdateArticleStatus(AdminUtil.getLoginUser(),
					titleId, ArticleStatusEnum.DELETE.ordinal());
			if (res)
			{
				return ResultUtil.sucess();
			} else
			{
				return ResultUtil.fail("标题不存在");
			}
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
	@RequestMapping(value = "/admin/article/del/batch")
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
			List<Long> idA = new ArrayList<>();
			for (int i = 0; i < idArr.length; i++)
			{
				idA.add(Long.valueOf(idArr[i]));
			}
			articleManagementWrappedService.updateArticleStatusInBatch(user, idA, ArticleStatusEnum.DELETE.ordinal());
			return ResultUtil.sucess();
		} catch (Exception e)
		{
			e.printStackTrace();
			return ResultUtil.fail("error:" + e.getMessage());
		}
	}

	@RequestMapping(value = "/admin/article/detail/{articleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<ArticleDTO> showArticleDetail(@PathVariable Long articleId, HttpServletRequest request,
			HttpServletResponse response)
	{
		try
		{
			ArticleDTO articleDTO = articleService.findByArticleId(articleId);
			return ResultUtil.sucess(articleDTO);
		} catch (Exception e)
		{
			logger.error("[find article] occur error {}", e.getMessage());
			e.printStackTrace();
			return ResultUtil.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "/admin/article/delete", method =
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
		List<Long> articleIdList = new ArrayList<>();
		try
		{
			for (int i = 0; i < ids.length; i++)
			{
				articleIdList.add(Long.parseLong(ids[i]));
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
			Map<String, Object> p = new HashMap<>();
			p.put("articleIdList", articleIdList);
			deleteArticleCommand.execute(p);
			return ResultUtil.sucess();
		} catch (Exception e)
		{
			logger.error("[delete article] error {}", e.getMessage());
			e.printStackTrace();
			return ResultUtil.fail(e.getMessage());
		}
	}

	@RequestMapping(value = "/admin/article/update/typeOrStatus", method =
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
				List<Long> ids = new ArrayList<Long>();
				for (int i = 0; i < articleIds.length; i++)
				{
					ids.add(Long.parseLong(articleIds[i]));
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
						articleManagementWrappedService.updateArticleStatusInBatch(userDTO, ids, artilceStatus);
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

	@RequestMapping(value = "/admin/article/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
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
		List<Long> pictureIdList = new ArrayList<>();
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
			for (String string : pictureIds)
			{
				pictureIdList.add(Long.parseLong(string));
			}
			articleDTO.setPictureIds(pictureIdList);
			params.put("pictureStatus", PictureStatusEnum.Effective.ordinal());
			params.put("pictureIdList", pictureIdList);
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

	@RequestMapping(value = "/admin/file/upload")
	public PictureUploadResponseDTO uploadFile(@RequestParam("imgFile") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response)
	{
		PictureUploadResponseDTO pictureUploadResponseDTO = new PictureUploadResponseDTO();
		String articleId = request.getParameter("articleId");
		articleId = articleId.replaceAll(",", "");
		if (StringUtils.isEmpty(articleId))
		{
			pictureUploadResponseDTO.setError(0);
			pictureUploadResponseDTO.setMessage("缺失参数,请刷新页面重试");
			return pictureUploadResponseDTO;
		}
		String storePath = fileService.getStoreBasePath(IFileStrategy.IMG_TYPE) + File.separator
				+ fileService.getVisitPrefix(IFileStrategy.IMG_TYPE) + File.separator + articleId + File.separator;
		String newFileName = UUID.randomUUID().toString();
		String url = null;
		try
		{
			url = fileService.upload(file, storePath, newFileName, IFileStrategy.IMG_TYPE);
		} catch (Exception e)
		{
			e.printStackTrace();
			pictureUploadResponseDTO.setError(1);
			pictureUploadResponseDTO.setMessage(e.getMessage());
			return pictureUploadResponseDTO;
		}

		PictureDTO pictureDTO = new PictureDTO();
		pictureDTO.setPictureUrl(url);
		pictureDTO.setArticleId(Long.parseLong(articleId));
		pictureDTO.setCreateDate(new Date());
		pictureDTO.setPictureStatus(PictureStatusEnum.Invalid.ordinal());
		try
		{
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
	@RequestMapping(value = "/admin/user/updateStatus", method =
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
	@RequestMapping(value = "/admin/user/find", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

	@RequestMapping(value = "/admin/user/update", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String> updateUserInfo(DlxyUser user, HttpServletRequest request, HttpServletResponse response)
	{
		UserDTO loginUser = AdminUtil.getLoginUser();
		String able = request.getParameter("able");
		// 或者直接属性初始化为null
		if (able.equals("0"))
		{
			user.setAble(false);
		} else
		{
			user.setAble(true);
		}
		String error = null;
		if (null == user.getUserId() || user.getUserId() < 0)
		{
			error = "用户id不存在";
		} else if (!user.getUserId().equals(loginUser.getUserId()) && !loginUser.isAdmin())
		{
			// params.put("error", "无权修改他人信息");
			logger.error("[更新用户信息]用户:{} 试图修改他人信息", loginUser.getRealname());
			IllegalLogDTO illegalLogDTO = new IllegalLogDTO(CommonUtils.getRemortIP(request), loginUser.getUserId(),
					"试图修改他人信息", IllegalLevelEnum.Serious.ordinal());
			throw new DlxySystemIllegalException(illegalLogDTO);
		}

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
		if (!StringUtils.isEmpty(error))
		{
			return ResultUtil.fail(error);
		}
		try
		{
			error = userManagementWrappedService.updateUser(loginUser, user);
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("[更新用户信息] error:{} cause:{}", e.getMessage(), e.getCause());
			error = e.getMessage();
		}
		if (StringUtils.isEmpty(error))
		{
			return ResultUtil.sucess();
		}
		return ResultUtil.fail(error);

	}

	@RequiresRoles("admin")
	@RequestMapping(value = "/admin/user/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<String> deleteUser(HttpServletRequest request, HttpServletResponse response)
	{
		String ids = request.getParameter("ids");

		if (StringUtils.isEmpty(ids))
		{
			return ResultUtil.fail("参数不能为空");
		}
		String[] userIdArray = ids.split(",");
		UserDTO userDTO = AdminUtil.getLoginUser();
		// if (!userDTO.isAdmin())
		// {
		// IllegalLogDTO illegalLogDTO = new
		// IllegalLogDTO(CommonUtils.getRemortIP(request), userDTO.getUserId(),
		// "试图删除用户", IllegalLevelEnum.Serious.ordinal());
		// PrintWriter writer = null;
		// try
		// {
		// writer = response.getWriter();
		// writer.write(JsonUtil.obj2Json(ResultUtil.fail("无权删除,记录你的信息了")));
		// } catch (IOException e)
		// {
		// e.printStackTrace();
		// }
		// throw new DlxySystemIllegalException("无权删除用户",illegalLogDTO);
		// }
		List<Long> userIdS = new LinkedList<Long>();
		for (String string : userIdArray)
		{
			userIdS.add(Long.parseLong(string));
		}
		if (userIdS.size() == 1)
		{
			userManagementWrappedService.deleteUserSingle(userDTO, userIdS.iterator().next());
		} else
		{
			userManagementWrappedService.deleteUser(userDTO, userIdS);
		}
		return ResultUtil.sucess();
	}

	/*
	 * 列出所有的角色
	 */
	// @RequiresRoles(value= { "admin","subAdmin"
	// })
	@RequestMapping(value = "/admin/user/roles/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
