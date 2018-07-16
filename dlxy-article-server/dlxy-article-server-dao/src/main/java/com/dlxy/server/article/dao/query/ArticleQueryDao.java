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
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

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
	
	
//	Collection<ArticleDTO>findArticlesInTitleIds(List<Integer> titelIds,int limit) throws SQLException;
	
	/*
	 * 用于前端展示的时候,显示每个标题下前几的文章
	 */
	Collection<ArticleDTO>findArticlesInTitleIds(Map<String, Object>params) throws SQLException;
	
//	ArticleDTO findByArticleId(Long articleId) throws SQLException;
	
//	int rollBackArticle(int status,Long articleId,Integer titleId) throws SQLException;
	
	
	void update(ArticleDTO articleDTO) throws SQLException;
	
}
