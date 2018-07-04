/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午2:59:22
* 
*/
package com.dlxy.server.article.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Observer;

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
//	Collection<ArticleDTO>findAllArticlesExceptRecommend(int start,int end);
	
	void updateArticleStatus(Long articleId,int status);
	
	void updateArticleStatusInBatch(Long[] articleIds,int status);
	
	
	Collection<ArticleDTO>findByParam(Map<String, Object>params,int start,int end) throws SQLException;
	
	ArticleDTO findByArticleId(Long articleId) throws SQLException;
	
	
	int rollBackArticle(int status,Long articleId,int titleId) throws SQLException;
	
	
	void insertOrUpdate(ArticleDTO articleDTO);
}
