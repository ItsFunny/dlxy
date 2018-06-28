/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午11:27:35
* 
*/
package com.dlxy.article.server.service.impl;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.article.server.query.ArticleCountQueryDao;
import com.dlxy.article.server.service.IArticleCountService;

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
	
	public Long countArticlesByDetailParam(Map<String, String> params) throws SQLException
	{
		return articleCountQueryDao.coutArticles(params);
	}

}
