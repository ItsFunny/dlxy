/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月30日 上午11:20:18
* 
*/
package com.dlxy.system.management.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.ResultDTO;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月30日 上午11:20:18
 */
@RestController
@RequestMapping("/search")
public class SearchRestAPIController
{
	/*
	 * articleId,articleTitle,articleAuthor
	 */
	@RequestMapping("/article")
	public ResultDTO<Collection<ArticleDTO>> searchArticle(HttpServletRequest request, HttpServletResponse response)
	{
		return null;
	}
}
