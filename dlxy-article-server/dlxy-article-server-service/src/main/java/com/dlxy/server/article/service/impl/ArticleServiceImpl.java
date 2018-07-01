/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午3:03:45
* 
*/
package com.dlxy.server.article.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.common.annotation.UserRecordAnnotation;
import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.server.article.dao.mybatis.ArticleDao;
import com.dlxy.server.article.dao.query.ArticleQueryDao;
import com.dlxy.server.article.service.IArticleService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 下午3:03:45
*/
@Service
public class ArticleServiceImpl implements IArticleService
{
	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private ArticleQueryDao articleQueryDao;

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

	@UserRecordAnnotation(dealWay="update(delete):article")
	@Override
	public void updateArticleStatusInBatch(Long[] articleIds, int status)
	{
		Map<String, Object>params=new HashMap<String, Object>();
		if(status==ArticleStatusEnum.DELETE.ordinal())
		{
			params.put("deleteDate", new Date());
		}
		params.put("status", status);
		params.put("ids", articleIds);
		articleDao.updateInBatch(params);
	}

	@Override
	public Collection<ArticleDTO> findByParam(Map<String, Object> params, int pageSize, int pageNum) throws SQLException
	{
		return articleQueryDao.findByParam(params, pageSize, pageNum);
	}

	@Override
	public Collection<ArticleDTO> findByArticleId(Long articleId) throws SQLException
	{
		return articleQueryDao.findByArticleId(articleId);
	}

	@Override
	public int rollBackArticle(int status,Long articleId,int titleId) throws SQLException
	{
		return articleQueryDao.rollBackArticle(status, articleId, titleId);
	}

	/*
	 * need annotation here
	 */
	@Override
	public void insertOrUpdate(ArticleDTO articleDTO)
	{
		articleDao.insertOrUpdate(articleDTO);
	}

}
