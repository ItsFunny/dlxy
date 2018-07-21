/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午3:03:45
* 
*/
package com.dlxy.server.article.service.impl;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.common.annotation.UserRecordAnnotation;
import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.server.article.dao.mybatis.ArticleMybatisDao;
import com.dlxy.server.article.dao.mybatis.TitleMybatisDao;
import com.dlxy.server.article.dao.mybatis.count.UserArticleDao;
import com.dlxy.server.article.dao.query.ArticleQueryDao;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.IUserArticleService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 下午3:03:45
*/
@Service
public class ArticleServiceImpl implements IArticleService ,IUserArticleService
{
	@Autowired
	private ArticleMybatisDao articleDao;
	@Autowired
	private UserArticleDao userArticleDao;
	@Autowired
	private ArticleQueryDao articleQueryDao;
	@Autowired
	private TitleMybatisDao titleMybatisDao;

//	public Collection<ArticleDTO> findAllArticlesExceptRecommend(int start,int end)
//	{
//		Collection<ArticleDTO> collection = articleDao.findAllExpectRecommendByPage(start,end);
//		return collection;
//	}

	@Override
	public void updateArticleStatus(Long articleId, int status)
	{
		articleDao.updateArticleStatus(articleId, status);
	}

//	@UserRecordAnnotation(dealWay="update(delete):article")
	@Override
	public void updateArticleStatusInBatch(Long[] articleIds, int status)
	{
		Map<String, Object>params=new HashMap<String, Object>();
		params.put("updateDate", new Date());
		if(status==ArticleStatusEnum.DELETE.ordinal())
		{
			params.put("deleteDate", new Date());
		}
		params.put("status", status);
		params.put("list", Arrays.asList(articleIds));
		articleDao.updateInBatch(params);
	}

	@Override
	public void updateArticleTypeInbatch(Long[] articleIds, int type)
	{
		Map<String, Object>params=new HashMap<String, Object>();
		params.put("updateDate", new Date());
		params.put("type", type);
		params.put("list", Arrays.asList(articleIds));
		articleDao.updateInBatch(params);
	}

	
	@Override
	public Collection<ArticleDTO> findByParam(Map<String, Object> params, int pageSize, int pageNum) throws SQLException
	{
		return articleQueryDao.findByParam(params, pageSize, pageNum);
	}

	@Override
	public ArticleDTO findArticleDetailByArticleId(Long articleId) throws SQLException
	{
		return articleDao.findArticleDetailByArticleId(articleId);
	}

	@Override
	public int rollBackArticle(int status,Long articleId,int titleId) throws SQLException
	{
		return articleDao.rollBackArticle(status, articleId, titleId);
	}

	@Override
	public void insertOrUpdate(ArticleDTO articleDTO)
	{
		articleDao.insertOrUpdate(articleDTO);
	}

	@Override
	public void addUserArticle(Long userId, Long articleId, String username)
	{
		userArticleDao.addUserArticle(userId, articleId, username);
	}

	@Override
	public void update(ArticleDTO articleDTO) throws SQLException
	{
		articleQueryDao.update(articleDTO);
	}

	@Override
	public Collection<ArticleDTO> findAllArticlesByPage(int start, int end)
	{
		return articleDao.findAllArtilcesByPage(start, end);
	}

	@Override
	public Long countAllArticles()
	{
		return articleDao.countAllArticles();
	}

	@Override
	public void deleteArticlesInBatch(Long[] articleIds)
	{
		articleDao.deleteInBatch(articleIds);
	}

	@Override
	public Collection<ArticleDTO> findAllRecommendArticles()
	{
		return articleDao.findAllRecommedArticles();
	}

	@Override
	public ArticleDTO findByArticleId(Long articleId)
	{
		return articleDao.findByArticleId(articleId);
	}

//	@Override
//	public Collection<ArticleDTO> findArtilcesByTilteIds(List<Integer> titleIds, int limit) throws SQLException
//	{
//		return articleQueryDao.findArticlesInTitleIds(titleIds, limit);
//	}

	@Override
	public Collection<ArticleDTO> findArtilcesByTilteIdsAndPage(int pageSize,int pageNum,List<Integer> ids) throws SQLException
	{
		return articleDao.findArticlesInTitleIdsByPage((pageNum-1)*pageSize, pageSize, ids);
	}
	@Override
	public Collection<ArticleDTO> findArticlesByTitleId(int pageSize, int pageNum, int titleId,int status)
	{
		return articleDao.findArticlesByTitleId((pageNum-1)*pageSize,pageSize, titleId,status);
	}


	@Override
	public Collection<ArticleDTO> findArticlesInTitleIdsTopNumber(List<Integer> ids, int limit,Integer status) throws SQLException
	{
		Collection<ArticleDTO> articleDTOs = articleDao.findArticlesInTitleIdsLimited(ids, limit,status);
		return articleDTOs;
	}

	@Override
	public Collection<ArticleDTO> findLatestArticleLimited(int count)
	{
		return articleDao.findLatestArticleLimited(count);
	}

	@Override
	public ArticleDTO findArticlePrevAndNext(Long articleId)
	{
		ArticleDTO articleDTO = findByArticleId(articleId);
		if(null==articleDTO)
		{
			return null;
		}
		Collection<ArticleDTO> collection = articleDao.findArticlePrevAndNext(articleId);
		for (ArticleDTO articleDTO2 : collection)
		{
			if(articleDTO2.getCreateDate().before(articleDTO.getCreateDate()))
			{
				articleDTO.setPrevious(articleDTO2);
			}else {
				articleDTO.setNext(articleDTO2);
			}
		}
//		collection.forEach(a->{
//			if(a.getCreateDate().before(articleDTO.getCreateDate()))
//			{
//				articleDTO.setPrevious(a);
//			}else {
//				articleDTO.setNext(a);
//			}
//		});
		return articleDTO;
	}

	@Override
	public Collection<ArticleDTO> findArticlesByParentTitleId(int pageSize, int pageNum, int titleParentId, int status)
	{
		return articleDao.findArticlesByParentTitleId((pageNum-1)*pageSize,pageSize, titleParentId, status);
	}

	
//	@Override
//	public Collection<ArticleDTO> findArticlesByTitleIds(Integer[] ids)
//	{
//		return articleDao.findByTitleIds(ids);
//	}


}
