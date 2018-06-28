/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午3:03:45
* 
*/
package com.dlxy.article.server.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.article.server.mybatis.ArticleDao;
import com.dlxy.article.server.service.IArticleService;
import com.dlxy.common.dto.ArticleDTO;

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

	public Collection<ArticleDTO> findAllArticlesExceptRecommend(int start,int end)
	{
		Collection<ArticleDTO> collection = articleDao.findAllExpectRecommendByPage(start,end);
		return collection;
	}

}
