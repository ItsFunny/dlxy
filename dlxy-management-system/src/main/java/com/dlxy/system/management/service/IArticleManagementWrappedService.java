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
public interface IArticleManagementWrappedService
{
	// PageDTO<Collection<ArticleDTO>>findArticles(int start,int end,Map<String,
	// Object>params) throws SQLException;

	PageDTO<Collection<ArticleDTO>> findByParams(int start, int end, Map<String, Object> params) throws SQLException;

	Collection<ArticleDTO> findByArticleId(Long articleId) throws SQLException;
//	void updateArticleStatusInBatch(Long[] articleIds, int status);
//	void delArticle(Long userId,Long articleId);

	void updateArticlesInBatch(Long userId, Long[] articleIds, int status);
	
	int rollBackArticle(Long userId,int status,Long articleId,int titleId) ;
	
	void insertOrUpdate(ArticleDTO articleDTO);

}
