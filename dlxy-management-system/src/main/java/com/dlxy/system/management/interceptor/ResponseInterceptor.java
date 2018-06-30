/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月30日 上午10:34:30
* 
*/
package com.dlxy.system.management.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月30日 上午10:34:30
*/
public class ResponseInterceptor implements HandlerInterceptor
{

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception
	{
		response.setContentType("application/json");
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception
	{
		System.out.println("post handler");
		response.setContentType("application/json");
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		System.out.println("pre handler");
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	

}
