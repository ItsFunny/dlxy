/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月9日 下午9:05:40
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
* 显示某一标题下的文章
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月9日 下午9:05:40
*/
@Controller
@RequestMapping("/title")
public class TitleController
{
	@RequestMapping("/detail")
	public ModelAndView detail(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView=new ModelAndView("article_detail");
		
		
		return modelAndView;
	}
}
