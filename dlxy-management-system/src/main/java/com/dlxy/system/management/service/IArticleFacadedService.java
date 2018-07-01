/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午11:29:52
* 
*/
package com.dlxy.system.management.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.PageDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 下午11:29:52
*/
public interface IArticleFacadedService
{
//	PageDTO<Collection<ArticleDTO>>findArticles(int start,int end,Map<String, Object>params) throws SQLException;
	
	PageDTO<Collection<ArticleDTO>>findByParams(int start,int end,Map<String, Object>params) throws SQLException;
	
//	void delArticle(Long userId,Long articleId);
	
//	void updateArticlesInBatch(Long userId,Long[] articleIds,int status);
	
	
}
