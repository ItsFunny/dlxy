/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月30日 上午11:45:47
* 
*/
package com.dlxy.server.article.dao.query;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import com.dlxy.common.dto.ArticleDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月30日 上午11:45:47
*/
public interface ArticleQueryDao
{
	Collection<ArticleDTO> findByParam(Map<String, Object>params,int pageSize,int pageNum) throws SQLException;
	
	ArticleDTO findByArticleId(Long articleId) throws SQLException;
	
	int rollBackArticle(int status,Long articleId,Integer titleId) throws SQLException;
	
	
	void update(ArticleDTO articleDTO) throws SQLException;
	
}
