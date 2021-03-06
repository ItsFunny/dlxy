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
import java.util.List;
import java.util.Map;
import java.util.Observer;

import org.apache.ibatis.annotations.Param;

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
	
	
	void updateArticleStatusByParentId(Integer titleParentId,int status);
	
	void updateArticleStatusByTitleId(Integer titleId,int status);
	
	
	void updateArticleStatus(Long articleId,int status);
	
	//修改文章状态
	void updateArticleStatusInBatch(List<Long> articleIds,int status);
	
	//修改文章类型
	void updateArticleTypeInbatch(List<Long>articleIds,int type);
	
	Integer updateInBatchSelective(Collection<ArticleDTO>articleDTOs);
	
//	void updateArticleStatusInBatch(Long[] articleIds,int status);
	
	/*
	 * search专用
	 */
	Collection<ArticleDTO>findByParam(Map<String, Object>params,int start,int end) throws SQLException;
	
	//好像不需要,已经写过了
//	Collection<ArticleDTO>findArticlesByTitleIds(Integer[] ids);
	
	
	
	@Deprecated
	Long countAllArticles();
	
	Collection<ArticleDTO>findAllArticlesByPage(int start,int end);
	
//	Collection<ArticleDTO>findArtilcesByTilteIds(List<Integer>titleIds,int limit) throws SQLException;
	
	
	//一口气查询父类下的所有的文章,及其父类
	List<ArticleDTO>findAllArticlesByTitleParentId(Integer titleParentId);
	//需要更改
	Collection<ArticleDTO>findArtilcesByTilteIdsAndPage(int pageSize,int pageNum,List<Integer> ids) throws SQLException;
	//查询单个的,为什么要单个,因为可以根据索引查,快,in查询有时候不走索引
	List<ArticleDTO>findArticlesByTitleId(int pageSize,int pageNum,int titleId,int status);
	//通过父节点查找 
	List<ArticleDTO>findArticlesByParentTitleId(int pageSize,int pageNum,int titleParentId,int status);
	
	
	
	/*
	 * 用于前端展示需要显示多少文章
	 */
	Collection<ArticleDTO>findArticlesInTitleIdsTopNumber(List<Integer>ids,int limit,Integer status) throws SQLException;
	/*
	 * 查询所有推荐文章
	 */
	@Deprecated
	Collection<ArticleDTO>findAllRecommendArticles();
	
	
	/*
	 * 找寻文章及其具体相关内容
	 */
	ArticleDTO findArticleDetailByArticleId(Long articleId) throws SQLException;
	
	/*
	 * 特定使用,不可复用,找寻titleId下的具体IDE文章,包括文章内容
	 */
	List<ArticleDTO>findArticlesByTitleId(Integer titleId,Integer status);
	
	/*
	 * 单单只是找寻文章,没有额外的信息
	 */
	ArticleDTO findByArticleId(Long articleId);
	

	
	
	/*
	 * 查询最新的几篇文章
	 */
	Collection<ArticleDTO> findLatestArticleLimited(int count);
	
	/**
	 * 搜索上一篇文章和下一篇文章
	 * @param articleId
	 * @return
	 * @author joker 
	 * @date 创建时间：2018年7月19日 下午9:48:55
	 */
	ArticleDTO findArticlePrevAndNext(Long articleId);
	
	
	int rollBackArticle(int status,Long articleId,int titleId) throws SQLException;
	
	
	void insertOrUpdate(ArticleDTO articleDTO);
	
	void update(ArticleDTO articleDTO) throws SQLException;
	
	void updateArticleVisitCount(Long articleId,Integer visitCount);
	
	Integer deleteArticlesInBatch(List<Long> articleIds);
	
	
}
