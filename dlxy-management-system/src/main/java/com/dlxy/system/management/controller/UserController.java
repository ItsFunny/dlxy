/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 下午3:24:18
* 
*/
package com.dlxy.system.management.controller;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.common.event.AppEvent;
import com.dlxy.common.event.AppEventPublisher;
import com.dlxy.common.event.Events;
import com.dlxy.common.vo.PageVO;
import com.dlxy.server.user.service.IUserRecordService;
import com.dlxy.server.user.service.IUserService;
import com.dlxy.system.management.model.FormUser;
import com.dlxy.system.management.service.IUserMangementWrappedService;
import com.dlxy.system.management.utils.ManagementUtil;
import com.joker.library.utils.CommonUtils;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月2日 下午3:24:18
 */
@Controller
@RequestMapping("/user")
public class UserController
{
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private IUserMangementWrappedService userManagementWrappedService;

	@Autowired
	private IUserService userService;

	@Autowired
	public AppEventPublisher eventPublisher;

	@Autowired
	private IUserRecordService userRecordService;

	Pattern realNamePattern = Pattern.compile("^([\u4e00-\u9fa5]{1,20}|[a-zA-Z]+ [a-zA-Z]+)$");

	// @RequiresRoles
	@RequestMapping(value = "/all")
	public ModelAndView showAllUser(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		Map<String, Object> params = new HashMap<>();
		UserDTO user = ManagementUtil.getLoginUser();
		params.put("user", user);
		Map<String, Object> p = new HashMap<>();
		String pageSizeStr = request.getParameter("pageSize");
		String pageNumStr = request.getParameter("pageNum");
		Integer pageSize = StringUtils.isEmpty(pageSizeStr) ? 1 : Integer.parseInt(pageSizeStr);
		Integer pageNum = StringUtils.isEmpty(pageNumStr) ? 1 : Integer.parseInt(pageNumStr);

		// String userIdStr=request.getParameter("userId");
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
		modelAndView = new ModelAndView("users", params);
		return modelAndView;
	}

	// @RequiresRoles
	@RequestMapping(value = "/add")
	public ModelAndView addUser(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("user_add");
		modelAndView.addObject("user",ManagementUtil.getLoginUser());
		return modelAndView;
	}

	// @RequiresRoles
	@RequestMapping(value = "/doAddUser")
	public ModelAndView doAddUser(FormUser formUser, BindingResult result, Model model, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView modelAndView = null;
		Map<String, Object> params = model.asMap();
		if (result.hasErrors())
		{
			params.put("error", result.getAllErrors());
		}
		UserDTO userDTO = new UserDTO();
		if (StringUtils.isEmpty(formUser.getRealname()))
		{
			params.put("error", "姓名不可为空");
		} else if ( !CommonUtils.validString(formUser.getRealname()))
		{
			params.put("error", "姓名格式错误,正确格式为: 王小二或者Justin Bieber");
		}
		if (!params.containsKey("error"))
		{
			formUser.to(userDTO);
			String ip = CommonUtils.getRemortIP(request);
			userDTO.setLastLoginIp(ip);

			try
			{
				userManagementWrappedService.addUser(ManagementUtil.getLoginUser().getUserId(), userDTO);
			} catch (Exception e)
			{
				e.printStackTrace();
				logger.error("[add user]error {}", e.getMessage());
				params.put("error", "姓名冲突,请更换姓名");
			}
			if (!params.containsKey("error"))
			{
				params.put("error", "添加成功");
				modelAndView=new ModelAndView("redirect:/user/all.html",params);
				return modelAndView;
			}
		}
		modelAndView = new ModelAndView("error", params);
		return modelAndView;
	}

	/*
	 * 只允许让admin或者是本人访问,其他人不可访问,若访问则会记录信息
	 */
	@RequestMapping("/releases")
	public ModelAndView showMyRelases(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize,
			@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum)
	{
		UserDTO user = ManagementUtil.getLoginUser();
		Long cuserId = user.getUserId();
		ModelAndView modelAndView = null;
		Map<String, Object> params = new HashMap<>();
		String cuserIdStr = request.getParameter("userId");
		if (!StringUtils.isEmpty(cuserIdStr))
		{
			cuserId = Long.parseLong(cuserIdStr);
			if (!user.getUserId().equals(cuserId) && !user.isAdmin())
			{
				// //发布消息,记录ip ,额外创建一个对象更加合适,用于安全检测
				// HashMap<String, Object>p=new HashMap<>();
				// p.put("ip", CommonUtils.getRemortIP(request));
				// p.put("reason", "try over access to get data");
				// eventPublisher.publish(new AppEvent(p, Events.IpCheck.name()));
				// logger.warn("illegal operation at {} with the ip {} user:{}", new Date(),
				// user.getUserId());
				//
				// p.put("userId", loginUser.getUserId());
				// p.put("illegalDetail", "试图恢复正常状态下的文章或者试图恢复不存在的文章");
				// p.put("illegalLevel", IllegalLevelEnum.Suspicious.ordinal());
				// eventPublisher.publish(new AppEvent(data, Events.UserIllegalLog.name()));
				// //返回无权
				params.put("error", "u dont have the authority");
				return new ModelAndView("error", params);
			}
		}
		params.put("userId", cuserId);
		String articleStatusStr = request.getParameter("articleStatus");
		if (!StringUtils.isEmpty(articleStatusStr))
		{
			try
			{
				params.put("articleStatus", Integer.parseInt(articleStatusStr));
			} catch (NumberFormatException e)
			{
				e.printStackTrace();
				params.put("error", "illegal argument :articleStatus");
				return new ModelAndView("error", params);
			}
		}
		PageDTO<Collection<Map<String, Object>>> pageDTO = null;
		try
		{
			pageDTO = userManagementWrappedService.findUserArticlesByPage(pageSize, pageNum, params);
		} catch (SQLException e)
		{
			e.printStackTrace();
			params.put("error", e.getMessage());
			return new ModelAndView("error", params);
		}
		PageVO<Collection<Map<String, Object>>> pageVO = new PageVO<>(pageDTO.getData(), pageSize, pageNum,
				pageDTO.getTotalCount());
		params.put("pageVO", pageVO);
		params.put("user", user);
		modelAndView = new ModelAndView("my_releases", params);
		return modelAndView;
	}
	/*
	 * q和userId 以q 为主,如果q存在并且
	 */

	@RequestMapping("/record")
	public ModelAndView showMyRecord(HttpServletRequest request, HttpServletResponse response)
	{
		UserDTO user = ManagementUtil.getLoginUser();
		Long cuserId = null;
		ModelAndView modelAndView = null;
		String pageSizeStr = request.getParameter("pageSize");
		String pageNumStr = request.getParameter("pageNum");
		Integer pageSize = StringUtils.isEmpty(pageSizeStr) ? 5 : Integer.parseInt(pageSizeStr);
		Integer pageNum = StringUtils.isEmpty(pageNumStr) ? 1 : Integer.parseInt(pageNumStr);
		Map<String, Object> params = new HashMap<>();

		String q = request.getParameter("q");

		String userIdStr = request.getParameter("userId");
		/*
		 * search
		 */
		if(!StringUtils.isEmpty(q))
		{
			try
			{
				cuserId=Long.parseLong(q);
				params.put("userId", cuserId);
			} catch (NumberFormatException e)
			{
				params.put("q", q);
			}
		}else if(!StringUtils.isEmpty(userIdStr))
		{
			cuserId=Long.parseLong(userIdStr);
			params.put("userId", cuserId);
		}else {
			if(!user.isAdmin())
			{
				cuserId=user.getUserId();
				params.put("userId", cuserId);
			}
		}
		if(!user.isAdmin() && !user.getUserId().equals(cuserId))
		{
			String ip = CommonUtils.getRemortIP(request);
			logger.warn(
					"[show user records] someone try get the data over access : userId: {}  desition {} from ip :{}",
					user.getUserId(), userIdStr, ip);
			// 记录日志
			HashMap<String, Object> data = new HashMap<>();
			data.put("ip", ip);
			data.put("userId", user.getUserId());
			data.put("illegalDetail", "试图越权获取用户操作记录:" + user.getUserId() + ":" + cuserId);
			data.put("illegalLevel", IllegalLevelEnum.Suspicious.ordinal());
			eventPublisher.publish(new AppEvent(data, Events.UserIllegalLog.name()));
			params.put("error", "对不起你无权访问他人记录,现已记录你的相关信息");
			return new ModelAndView("error",params);
		}
		
		
		
//		if (!StringUtils.isEmpty(q))
//		{
//			try
//			{
//				cuserId = Long.parseLong(q);
//				if (user.isAdmin() || user.getUserId().equals(cuserId))
//				{
//					params.put("userId", cuserId);
//				}else {
//					
//				}
//			} catch (NumberFormatException e)
//			{
//				params.put("q", q);
//			}
//		}else {
//			
//		}
//
//		if (!StringUtils.isEmpty(userIdStr))
//		{
//			try
//			{
//				cuserId = Long.parseLong(userIdStr);
//				if (!user.getUserId().equals(cuserId) && !user.isAdmin())
//				{
//					String ip = CommonUtils.getRemortIP(request);
//					logger.warn(
//							"[show user records] someone try get the data over access : userId: {}  desition {} from ip :{}",
//							user.getUserId(), userIdStr, ip);
//					// 记录日志
//					HashMap<String, Object> data = new HashMap<>();
//					data.put("ip", ip);
//					data.put("userId", user.getUserId());
//					data.put("illegalDetail", "试图越权获取用户操作记录:" + user.getUserId() + ":" + cuserId);
//					data.put("illegalLevel", IllegalLevelEnum.Suspicious.ordinal());
//					eventPublisher.publish(new AppEvent(data, Events.UserIllegalLog.name()));
//					params.put("error", "sorry u dont have the authorizty");
//					return new ModelAndView("error", params);
//				}
//				params.put("userId", cuserId);
//			} catch (NumberFormatException e)
//			{
//
//			} catch (Exception e)
//			{
//				String ip = CommonUtils.getRemortIP(request);
//				logger.warn("[show user records] someone put the invalid parameter : {} from ip :{}", userIdStr, ip);
//				params.put("error", "invalid argument");
//				modelAndView = new ModelAndView("error", params);
//				return modelAndView;
//			}
//		} else
//		{
//			if (!user.isAdmin())
//			{
//				params.put("userId", cuserId);
//			}
//		}

		try
		{
			PageDTO<Collection<UserRecordDTO>> pageDTO = userManagementWrappedService.findUserRecords(pageSize, pageNum,
					params);
			PageVO<Collection<UserRecordDTO>> pageVO = new PageVO<Collection<UserRecordDTO>>(pageDTO.getData(),
					pageSize, pageNum, pageDTO.getTotalCount());
			params.put("pageVO", pageVO);
			params.put("user", user);
			modelAndView = new ModelAndView("records", params);
		} catch (SQLException e)
		{
			logger.error("[显示我的操作记录] error ,{}", e.getMessage());
			e.printStackTrace();
			modelAndView = new ModelAndView("error");
			modelAndView.addObject("error", e.getMessage());
		}
		return modelAndView;
	}

}
