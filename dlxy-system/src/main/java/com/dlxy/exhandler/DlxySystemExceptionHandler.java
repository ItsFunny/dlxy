/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月14日 下午6:52:55
* 
*/
package com.dlxy.exhandler;

import java.util.HashMap;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.event.AppEvent;
import com.dlxy.common.event.AppEventPublisher;
import com.dlxy.common.event.Events;
import com.dlxy.exception.DlxySuspicionException;
import com.dlxy.exception.DlxySystemIllegalException;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月14日 下午6:52:55
*/
@ControllerAdvice
public class DlxySystemExceptionHandler
{
	@Autowired
	private AppEventPublisher appeventPublisher;
	@ExceptionHandler(value=DlxySystemIllegalException.class)
	public ModelAndView recordIp(DlxySystemIllegalException dlxySuspicionException)
	{
		//返回404 界面
		IllegalLogDTO illegalLogDTO = dlxySuspicionException.getIllegalLogDTO();
		AppEvent event=new AppEvent();
		event.setEventType(Events.UserIllegalLog.name());
		event.setData(illegalLogDTO);
		appeventPublisher.publish(event);
		ModelAndView modelAndView=new ModelAndView("404");
		return modelAndView;
	}
	@ExceptionHandler(value=UnauthorizedException.class)
	public ModelAndView unAuth()
	{
		return new ModelAndView("no_permission");
	}

}
