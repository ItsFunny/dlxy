/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午8:53:03
* 
*/
package com.dlxy.system.management.exhandler;


import java.util.HashMap;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.dto.ResultDTO;
import com.dlxy.common.event.AppEvent;
import com.dlxy.common.event.AppEventPublisher;
import com.dlxy.common.event.Events;
import com.dlxy.common.utils.ResultUtil;
import com.dlxy.system.management.exception.ManagementException;
import com.dlxy.system.management.exception.ManagementExceptionEnum;
import com.dlxy.system.management.exception.ManagementIllegalException;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月29日 下午8:53:03
*/
@ControllerAdvice
public class ManagementExceptionHandler
{
	@Autowired
	private AppEventPublisher eventPublisher;
	@ExceptionHandler(ManagementException.class)
	public ModelAndView handleManagementException(ManagementException exception)
	{
		ModelAndView modelAndView=null;
		if(exception.getId().equals(ManagementExceptionEnum.USER_NOT_LOGIN.ordinal()))
		{
			modelAndView=new ModelAndView("redirect:/login.html?error=请先登录");
			return modelAndView;
		}
		return modelAndView;
	}
	@ExceptionHandler(UnauthorizedException.class)
	public ModelAndView unAuthorization()
	{
		ModelAndView modelAndView=new ModelAndView("unauth");
		modelAndView.addObject("error","无权访问");
		return modelAndView;
	}
	@ExceptionHandler(ManagementIllegalException.class)
	public ResultDTO<?>recordIllegalInfo(ManagementIllegalException exception)
	{
		IllegalLogDTO illegalLogDTO = exception.getIllegalLogDTO();
		if(null!=illegalLogDTO)
		{
			HashMap<String, Object>data=new HashMap<>();
			data.put("illegalLogDTO",illegalLogDTO);
			eventPublisher.publish(new AppEvent(data, Events.UserIllegalLog.name()));
		}
		return ResultUtil.fail("无权访问");
	}
}
