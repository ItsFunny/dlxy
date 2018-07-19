/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月14日 下午6:52:11
* 
*/
package com.dlxy.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.enums.DlxyTitleEnum;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.service.ITitleManagementWrappedService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月14日 下午6:52:11
*/
@Controller
public class PortalController
{
	private Logger logger=LoggerFactory.getLogger(PortalController.class);
	@Autowired
	private ITitleService titleService;
	@Autowired
	private ITitleManagementWrappedService titleManagementWrappedService;
	@Autowired
	private IArticleService articleService;
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response)
	{
		/*
		 * 1.得到所有的类目
		 * 2.得到类目对应的文章
		 */
		Map<String, Object>params=new HashMap<String, Object>();
		ModelAndView modelAndView=null;
		DlxyTitleDTO dlxyTitleDTO = titleManagementWrappedService.findDlxyDetailTitles();
		params.put("dlxyTitleDTO", dlxyTitleDTO);
		modelAndView=new ModelAndView("portal/index",params);
		return modelAndView;
	}
	
	@RequestMapping("/title/detail/{titleId}")
	public ModelAndView showTitleDetail(@PathVariable("titleId")Integer titleId,HttpServletRequest requestq,HttpServletResponse response)
	{
		ModelAndView modelAndView=null;
		
		return modelAndView;
	}
	@RequestMapping("/article/detail/{articleId}")
	public ModelAndView showArticleDetail(@PathVariable("articleId")String articleIdStr,HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView=null;
		Map<String, Object>params=new HashMap<>();
		try
		{
			Long articleId=Long.parseLong(articleIdStr);
			ArticleDTO articleDTO = articleService.findArticlePrevAndNext(articleId);
			params.put("article", articleDTO);
			//不知道为什么,前端ajax 获取到数据了,但是滚轮无法转动,所以这里也直接从这里取吧,可以解决,但不是我的范围了,交给前端的人解决
			
			modelAndView=new ModelAndView("portal/article_detail",params);
		} catch (Exception e)
		{
			params.put("error", "参数错误:");//记录消息
			logger.error("[show article detail] error:{}",e.getMessage());
			modelAndView=new ModelAndView("error",params);
		}
		return modelAndView;
	}
}

