/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月14日 下午6:51:43
* 
*/
package com.dlxy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月14日 下午6:51:43
*/
@Controller
@RequestMapping("/admin")
public class AdminController
{
	@RequestMapping("/test")
	public ModelAndView test(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView=new ModelAndView("test");
		return modelAndView;
	}
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response)
	{
		
		ModelAndView modelAndView=null;
		
		
		return modelAndView;
	}
}
