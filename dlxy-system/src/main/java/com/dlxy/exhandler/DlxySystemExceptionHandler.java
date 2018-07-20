/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月14日 下午6:52:55
* 
*/
package com.dlxy.exhandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.exception.DlxySuspicionException;

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
	@ExceptionHandler(value=DlxySuspicionException.class)
	public ModelAndView recordIp(DlxySuspicionException dlxySuspicionException)
	{
		//返回404 界面
		ModelAndView modelAndView=new ModelAndView("404");
		
		return modelAndView;
	}

}
