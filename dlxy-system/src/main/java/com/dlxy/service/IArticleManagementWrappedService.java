/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午11:29:52
* 
*/
package com.dlxy.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.UserDTO;

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
	
	//显示所有的,包括异常的那些文章
	PageDTO<Collection<ArticleDTO>>findAllArticles(int start,int end);
	
	PageDTO<Collection<ArticleDTO>>findByTitleIds(int pageSize,int pageNum,List<Integer> ids) throws SQLException;

	PageDTO<Collection<ArticleDTO>> findByParams(int pageSize, int pageNum, Map<String, Object> params) throws SQLException;

	ArticleDTO findByArticleId(Long articleId) throws SQLException;
//	void updateArticleStatusInBatch(Long[] articleIds, int status);
//	void delArticle(UserDTO userDTO,Long articleId);

	void updateArticlesInBatch(UserDTO userDTO, Long[] articleIds, int status);
	
	void updateArticleTypeInBatch(UserDTO userDTO,Long[] articleIds,int type);
	
	void updateArticleByArticleId(ArticleDTO articleId) throws SQLException;
	
	int rollBackArticle(UserDTO userDTO,int status,Long articleId,int titleId) ;
	
	void insertOrUpdate(ArticleDTO articleDTO);
	
	
	
	void addTitleOrUpdate(UserDTO  userDTO,DlxyTitleDTO dlxyTitleDTO);
	
	void deleteByTitleId(UserDTO userDTO,Integer titleId);	
	
	void deleteInBatch(UserDTO userDTO,Long[] articleIds);
	

}
