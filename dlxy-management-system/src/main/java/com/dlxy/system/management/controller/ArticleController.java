/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午6:58:57
* 
*/
package com.dlxy.system.management.controller;


import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.ResultDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.common.service.IdWorkerService;
import com.dlxy.common.utils.ResultUtil;
import com.dlxy.common.vo.PageVO;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.system.management.model.FormArticle;
import com.dlxy.system.management.service.IArticleFacadedService;
import com.dlxy.system.management.utils.ManagementUtil;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月28日 下午6:58:57
 */
@Controller
@RequestMapping("/article")
public class ArticleController
{
	private Logger logger = LoggerFactory.getLogger(ArticleController.class);
	@Autowired
	@Qualifier("articleFacadedServiceImpl")
	private IArticleFacadedService articleFacadedService;

	@Autowired
	private IArticleService articleService;

	@Autowired
	private IdWorkerService idWorkerService;

	@RequestMapping("/all")
	public ModelAndView showAll(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		String error = request.getParameter("error");
		String pageSizeStr = request.getParameter("pageSize");
		String pageNumStr = request.getParameter("pageNum");
		int pageSize = StringUtils.isEmpty(pageSizeStr) ? 2 : Integer.parseInt(pageSizeStr);
		int pageNum = StringUtils.isEmpty(pageNumStr) ? 1 : Integer.parseInt(pageNumStr);
		Map<String, Object> p = new HashMap<>();
		if (params.containsKey("q"))
		{
			if (!StringUtils.isEmpty(params.get("q").toString()))
			{
				p.put("searchParam", params.get("q"));
			}
		}
		PageDTO<Collection<ArticleDTO>> pageDTO = null;
		try
		{
			pageDTO = articleFacadedService.findByParams((pageNum - 1) * pageSize, pageSize, p);
		} catch (SQLException e)
		{
			e.printStackTrace();
			params.put("error", e.getMessage());
		}
		if (params.containsKey("error"))
		{
			modelAndView = new ModelAndView("error", params);
			return modelAndView;
		}
		PageVO<Collection<ArticleDTO>> pageVO = new PageVO<Collection<ArticleDTO>>(pageDTO.getData(), pageSize, pageNum,
				pageDTO.getTotalCount());
		params.put("pageVO", pageVO);
		params.put("error", error);
		modelAndView = new ModelAndView("article_all", params);
		return modelAndView;
	}

	@RequestMapping("/deleted")
	public ModelAndView showAllDeletedArticles(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "pageSize", defaultValue = "2") String pageSizeStr,
			@RequestParam(name = "pageNum", defaultValue = "1") String pageNumStr)
	{
		ModelAndView modelAndView = null;
		int pageSize = Integer.parseInt(pageSizeStr);
		int pageNum = Integer.parseInt(pageNumStr);
		Map<String, Object> params = new HashMap<>();
		params.put("articleStatus", "2");
		try
		{
			PageDTO<Collection<ArticleDTO>> pageDTO = articleFacadedService.findByParams((pageNum - 1) * pageSize,
					pageSize, params);
			PageVO<Collection<ArticleDTO>> pageVO = new PageVO<Collection<ArticleDTO>>(pageDTO.getData(), pageSize,
					pageNum, pageDTO.getTotalCount());
			params.put("pageVO", pageVO);
			modelAndView = new ModelAndView("article_del", params);
		} catch (SQLException e)
		{
			e.printStackTrace();
			logger.error("[显示所有删除文章]error {}", new Date());
			params.put("error", e.getMessage());
			modelAndView = new ModelAndView("error", params);
		}
		return modelAndView;
	}

	// @RequestMapping("/del/{articleId}")
	// public ModelAndView delArtilce(@PathVariable Long articleId)
	// {
	// ModelAndView modelAndView = null;
	// Map<String, Object> params = new HashMap<>();
	// try
	// {
	// articleService.updateArticleStatusInBatch( new Long[] {articleId},
	// ArticleStatusEnum.DELETE.ordinal());
	// params.put("error", "删除成功");
	// modelAndView = new ModelAndView("redirect:/article/all.html", params);
	// } catch (Exception e)
	// {
	// e.printStackTrace();
	// params.put("error", "删除失败");
	// modelAndView = new ModelAndView("error", params);
	// }
	// return modelAndView;
	// }

	/*
	 * add article
	 */
	@RequestMapping("/add")
	public ModelAndView addArticle(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response)
	{
		long articleId = idWorkerService.nextId();
		params.put("articleId", articleId);
		ModelAndView modelAndView = new ModelAndView("article_add", params);
		return modelAndView;
	}
	
	/*
	 * 需要修改,需要test
	 */
	@RequestMapping("/doAddArticle")
	public ModelAndView doAddArticle(@Valid FormArticle formArticle, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		Map<String, Object>params=new HashMap<>();
		if(bindingResult.hasErrors())
		{
			params.put("error", bindingResult.getAllErrors().toString());
		}
		try
		{
			formArticle.isInvalid();
		} catch (Exception e)
		{
			e.printStackTrace();
			params.put("error", e.getMessage());
		}
		if(params.containsKey("error"))
		{
			modelAndView=new ModelAndView("error",params);
			return modelAndView;
		}
		ArticleDTO articleDTO=new ArticleDTO();
		formArticle.to(articleDTO);
		articleService.insertOrUpdate(articleDTO);
		//还需要修改图片的状态  今天先直接增加再说
		modelAndView=new ModelAndView("redirect:/article/all.html");
		return modelAndView;
	}

}
