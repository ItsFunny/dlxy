/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月8日 下午8:56:37
* 
*/
package com.dlxy.system.controller;

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
* @date 创建时间：2018年7月8日 下午8:56:37
*/
@Controller
public class IndexController
{
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView=null;
		
		modelAndView=new ModelAndView("index");
		return modelAndView;
	}

}
