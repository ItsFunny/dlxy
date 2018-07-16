/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月9日 下午9:09:17
* 
*/
package com.dlxy.system.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.server.article.service.IArticleService;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月9日 下午9:09:17
 */
@Controller
@RequestMapping("/article")
public class ArticleController
{

	@Autowired
	private IArticleService articleService;

	@RequestMapping("/detail/{articleId}")
	public ModelAndView articleDetail(@PathVariable Long articleId, HttpServletRequest request,
			HttpServletResponse response)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		ModelAndView modelAndView = null;
		ArticleDTO articleDTO = articleService.findByArticleId(articleId);
		params.put("articleDTO", articleDTO);
		
		modelAndView = new ModelAndView("article_detail", params);
		return modelAndView;
	}
}
