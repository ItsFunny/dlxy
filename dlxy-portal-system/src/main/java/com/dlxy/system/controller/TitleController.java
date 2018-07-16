/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月9日 下午9:05:40
* 
*/
package com.dlxy.system.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.server.article.service.IArticleService;

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
	@Autowired
	private IArticleService articleService;
	@RequestMapping("/detail/{titleId}")
	public ModelAndView detail(@PathVariable Integer titleId,HttpServletRequest request,HttpServletResponse response)
	{
		/*
		 * 1.显示这个类目下的所有子类目,在左边显示
		 * 2.中间显示具体文章 分页
		 * 3.最新公告ajax 获取
		 */
		Map<String, Object>params=new HashMap<String, Object>();
		ModelAndView modelAndView=new ModelAndView("article_detail");
		
		return modelAndView;
	}
}
