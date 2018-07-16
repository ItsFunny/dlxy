/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月8日 下午8:56:37
* 
*/
package com.dlxy.system.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;

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
	@Autowired
	private ITitleService titleService;
	@Autowired
	private IArticleService articleService;
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView=null;
		Map<String, Object>params=new HashMap<String, Object>();
		/*
		 * 1.find all parents
		 * 2. find top 5 child one articles
		 */
		Collection<DlxyTitleDTO> parents = titleService.findAllParent();
		List<Integer>parentIds=new ArrayList<Integer>();
		for (DlxyTitleDTO dlxyTitleDTO : parents)
		{
			//all parentTitleids
			parentIds.add(dlxyTitleDTO.getTitleId());
		}
	
		//所有父类下的第一个子类下的前5文章
		Collection<ArticleDTO> childs=null;
		try
		{
			Map<String, Object>p=new HashMap<>();
			p.put("ids", parentIds);
			p.put("limit", 5);
			childs = articleService.findArtilcesByTilteIds(p);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (DlxyTitleDTO dlxyTitleDTO : parents)
		{
			for (ArticleDTO articleDTO : childs)
			{
				if(articleDTO.getTitleParentId().equals(dlxyTitleDTO.getTitleId()))
				{
					dlxyTitleDTO.getArticles().add(articleDTO);
				}
				
			}
		}
		params.put("titleDTOLsit", parents);
		modelAndView=new ModelAndView("index",params);
		return modelAndView;
	}

}
