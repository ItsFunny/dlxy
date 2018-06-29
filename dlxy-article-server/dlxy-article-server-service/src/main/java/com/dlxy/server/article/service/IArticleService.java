/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午2:59:22
* 
*/
package com.dlxy.server.article.service;

import java.util.Collection;

import com.dlxy.common.dto.ArticleDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 下午2:59:22
*/
public interface IArticleService
{ 	
	//这个方法可以复用的,需要修改
	Collection<ArticleDTO>findAllArticlesExceptRecommend(int pageSize,int pageNum);
	
	void updateArticleStatus(Long articleId,int status);
}
