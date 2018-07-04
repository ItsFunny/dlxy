/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 下午3:24:18
* 
*/
package com.dlxy.system.management.controller;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.ArticleDTO;
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
			pageDTO = userManagementWrappedService.findByPage(pageSize, pageNum, params);
		} catch (SQLException e)
		{
			e.printStackTrace();
			params.put("error", e.getMessage());
			return new ModelAndView("error", params);
		}
		PageVO<Collection<Map<String, Object>>> pageVO = new PageVO<>(pageDTO.getData(), pageSize, pageNum,
				pageDTO.getTotalCount());
		params.put("pageVO", pageVO);
		modelAndView = new ModelAndView("my_releases", params);
		return modelAndView;
	}

	@RequestMapping("/record")
	public ModelAndView showMyRecord(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		String pageSizeStr = request.getParameter("pageSize");
		String pageNumStr = request.getParameter("pageNum");
		Integer pageSize = StringUtils.isEmpty(pageSizeStr) ? 1 : Integer.parseInt(pageSizeStr);
		Integer pageNum = StringUtils.isEmpty(pageNumStr) ? 1 : Integer.parseInt(pageNumStr);
		Map<String, Object> params = new HashMap<>();
		UserDTO user = ManagementUtil.getLoginUser();
		String userIdStr = request.getParameter("userId");
		if (!StringUtils.isEmpty(userIdStr))
		{
			try
			{
				long userId = Long.parseLong(userIdStr);
				if (user.isAdmin() || user.getUserId().equals(userId))
				{
					params.put("q", userId);
				} else
				{
					// 记录日志
					// HashMap<String, Object>data=new HashMap<>();
					// data.put("ip",ip);
					// data.put("detail", "try over access to get user records info ");
					// eventPublisher.publish(new AppEvent(data, Events.IpCheck.name()));
					params.put("q", user.getUserId());
				}
			} catch (Exception e)
			{
				String ip = CommonUtils.getRemortIP(request);
				logger.warn("[show user records] someone put the invalid parameter : {} from ip :{}", userIdStr, ip);
				params.put("error", "invalid argument");
				modelAndView = new ModelAndView("error", params);
				return modelAndView;
			}
		}

		try
		{
			PageDTO<Collection<UserRecordDTO>> pageDTO = userManagementWrappedService.findUserRecords(pageSize, pageNum,
					params);
			PageVO<Collection<UserRecordDTO>> pageVO = new PageVO<Collection<UserRecordDTO>>(pageDTO.getData(),
					pageSize, pageNum, pageDTO.getTotalCount());
			params.put("pageVO", pageVO);
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
