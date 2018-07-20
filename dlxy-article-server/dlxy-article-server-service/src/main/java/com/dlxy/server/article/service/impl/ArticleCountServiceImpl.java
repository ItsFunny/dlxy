/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午11:27:35
* 
*/
package com.dlxy.server.article.service.impl;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.server.article.dao.mybatis.ArticleMybatisDao;
import com.dlxy.server.article.dao.query.ArticleCountQueryDao;
import com.dlxy.server.article.dao.query.ArticleQueryDao;
import com.dlxy.server.article.service.IArticleCountService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 下午11:27:35
*/
@Service
public class ArticleCountServiceImpl implements IArticleCountService
{

	@Autowired
	private ArticleCountQueryDao articleCountQueryDao;
	
	@Autowired
	private ArticleMybatisDao articleMybatisDao;
	
	public Long countArticlesByDetailParam(Map<String, Object> params) throws SQLException
	{
		return articleCountQueryDao.coutArticles(params);
	}

	@Override
	public Long coutArtilcesByTitleIds(Integer[] titleIds)
	{
		return articleMybatisDao.countArticleByTitleIds(titleIds);
	}

	@Override
	public Long countTitleArticles(Integer titleId, Integer parentTitleId, Integer status) throws SQLException
	{
		return articleCountQueryDao.countTitleArticles(titleId, parentTitleId, status);
	}

}
