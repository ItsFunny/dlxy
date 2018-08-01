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
import com.dlxy.model.ArticleVisitInfo;
import com.dlxy.vo.ArticleVO;
import com.dlxy.vo.TitleDetailVO;

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
	
	/*
	 * 查询谋篇文章的访问人数
	 */
	Integer findArticleVisitCount(Long articleId,String ip);
	
	@Deprecated
	//显示所有的,包括异常的那些文章
	PageDTO<Collection<ArticleDTO>>findAllArticles(int start,int end);
	
	PageDTO<Collection<ArticleDTO>>findByTitleIds(int pageSize,int pageNum,List<Integer> ids) throws SQLException;

	PageDTO<Collection<ArticleDTO>> findByParams(int pageSize, int pageNum, Map<String, Object> params) throws SQLException;
	
	/*
	 * 查询某个分类下的文章 以及显示他的兄弟类或者子类
	 */
	TitleDetailVO findTitleArticles(int pageSize,int pageNum,int titleId) throws SQLException;

	ArticleDTO findArticleDetailByArticleId(Long articleId) throws SQLException;
	
	ArticleDTO showArticleDetail(Long articleId) throws SQLException;
//	void updateArticleStatusInBatch(Long[] articleIds, int status);
//	void delArticle(UserDTO userDTO,Long articleId);

	void updateArticlesInBatch(UserDTO userDTO, List<Long> articleIds, int status);
	
	void updateArticleTypeInBatch(UserDTO userDTO,List<Long> articleIds,int type);
	
	void updateArticleByArticleId(ArticleDTO articleId) throws SQLException;
	
	int rollBackArticle(UserDTO userDTO,int status,Long articleId,int titleId) ;
	
	void insertOrUpdate(ArticleDTO articleDTO);
	
	
	
	void addTitleOrUpdate(UserDTO  userDTO,DlxyTitleDTO dlxyTitleDTO);
	
	Integer deleteByTitleId(UserDTO userDTO,Integer titleId);	
	
	void deleteInBatch(UserDTO userDTO,List<Long> articleIds);
	

}
