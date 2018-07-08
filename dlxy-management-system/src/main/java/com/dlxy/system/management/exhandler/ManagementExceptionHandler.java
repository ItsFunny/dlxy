/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午8:53:03
* 
*/
package com.dlxy.system.management.exhandler;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.ResultDTO;
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
		String ip=exception.getIp();
		String detail=exception.getDetail();
		Long userId=exception.getUserId();
		//发布消息
		
		return null;
	}
}
