/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月14日 下午6:51:43
* 
*/
package com.dlxy.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.enums.DlxyTitleEnum;
import com.dlxy.common.vo.PageVO;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.service.IArticleManagementWrappedService;
import com.dlxy.utils.AdminUtil;

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
	private Logger logger=LoggerFactory.getLogger(AdminController.class);
	@Autowired
	private ITitleService titleService;
	@Autowired
	private IArticleManagementWrappedService articleManagementWrappedService;
	@RequestMapping("/test")
	public ModelAndView test(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView=new ModelAndView("test");
		return modelAndView;
	}
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response)
	{
		UserDTO user = AdminUtil.getLoginUser();
		ModelAndView modelAndView=new ModelAndView("index");
		modelAndView.addObject("user",user);
		return modelAndView;
	}
	/*
	 * 显示学院相关的类目
	 */
	@RequestMapping("/aboutTitles")
	public ModelAndView showAllTiltles(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView=new ModelAndView("about_titles");
//		Collection<DlxyTitleDTO> collection = titleService.findAllParent();
//		modelAndView.addObject("titles",collection);
		return modelAndView;
	}
	/*
	 * 显示新闻相关的类目
	 */
	@RequestMapping("/newsTitles")
	public ModelAndView showAllNewsTitles(HttpServletRequest requestq,HttpServletResponse response)
	{
		ModelAndView modelAndView=new ModelAndView("news_titles");
		Collection<DlxyTitleDTO> collection = titleService.findTitlesByType(DlxyTitleEnum.NEWS_TITLE.ordinal());
		DlxyTitleDTO dlxyTitleDTO=null;
		if(null!=collection &&! collection.isEmpty())
		{
			dlxyTitleDTO=collection.iterator().next();
			Collection<DlxyTitleDTO> childs = titleService.findChildsByParentId(dlxyTitleDTO.getTitleId());
			modelAndView.addObject("titles",childs);
		}
		modelAndView.addObject("title",dlxyTitleDTO);
		return modelAndView;
	}
	@RequestMapping("/title/article/{titleId}")
	public ModelAndView showTitleAllTiltles(@PathVariable("titleId") String titleIdStr,HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView=null;
		Map<String, Object>params=new HashMap<String, Object>();
		params.put("user", AdminUtil.getLoginUser());
		String pageSizeStr=StringUtils.defaultString(request.getParameter("pageSize"), "5");
		String pageNumStr=StringUtils.defaultString(request.getParameter("pageNum"),"1");
		Integer pageSize=Integer.parseInt(pageSizeStr);
		Integer pageNum=Integer.parseInt(pageNumStr);
		Integer titleId=null;
		try
		{
			titleId=Integer.parseInt(titleIdStr);
			Collection<DlxyTitleDTO> collection = titleService.findChildsByParentId(titleId);
			List<Integer>ids=new ArrayList<>();
			if(null != collection && !collection.isEmpty())
			{
				
				collection.stream().forEach(p->{
					ids.add(p.getTitleId());
				});
				params.put("titleParentId", titleId);
			}else {
				ids.add(titleId);
				params.put("titleIdStr", titleId);
			}
			
			PageDTO<Collection<ArticleDTO>> pageDTO = articleManagementWrappedService.findByTitleIds(pageSize, pageNum, ids);
			PageVO< Collection<ArticleDTO>>pageVO=new PageVO<Collection<ArticleDTO>>(pageDTO.getData(), pageSize, pageNum, pageDTO.getTotalCount());
			params.put("pageVO", pageVO);
		} catch (Exception e)
		{
			logger.error("[show articles belong to the title ] titleId:{} ,error:{},location:{}",titleId,e.getMessage(),e.getStackTrace());
			params.put("error", e.getMessage());
		}
		if(params.containsKey("error"))
		{
			modelAndView=new ModelAndView("error",params);
		}else {
			modelAndView=new ModelAndView("title_article_detail",params);
		}
		return modelAndView;
	}
}
