/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月10日 上午10:29:36
* 
*/
package com.dlxy.system.exhandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.exception.UnfindPageException;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月10日 上午10:29:36
*/
@ControllerAdvice
public class PortalExceptionHandler
{
	@ExceptionHandler(value=UnfindPageException.class)
	public ModelAndView unfindPage()
	{
		return new ModelAndView("404");
	}

}
