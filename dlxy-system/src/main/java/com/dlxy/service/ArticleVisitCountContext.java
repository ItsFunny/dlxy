/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月26日 下午1:21:10
* 
*/
package com.dlxy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dlxy.model.ArticleVisitCountFactory;
import com.dlxy.model.ArticleVisitInfo;
import com.dlxy.server.article.service.IArticleService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月26日 下午1:21:10
*/
@Component
public class ArticleVisitCountContext
{
	private AbstractArticleVistitCountStrategy strategy;
	
	@Autowired
	private IArticleService articleService;
	@Autowired
	private IRedisService redisService;
	
//	public ArticleVisitCountContext(IArticleService articleService,IRedisService redisService)
//	{
//		this.articleService=articleService;
//		this.redisService=redisService;
//	}
//	public void init(IArticleService articleService,IRedisService redisService)
//	{
//		this.articleService=articleService;
//		this.redisService=redisService;
//	}
	public Integer visitCount(Long articleId)
	{
		ArticleVisitInfo visitInfo = ArticleVisitCountFactory.get(articleId,redisService,articleService);
		if(redisService.isAvaliable())
		{
			this.strategy=new RedisArticleVIsitCountStrategy(redisService, articleService);
		}else {
			this.strategy=new DbVisitCountStrategy(articleService);
		}
		return this.strategy.visitAndIncr(visitInfo);
	}
//	public Integer visitCount(ArticleVisitInfo visitInfo)
//	{
//		return this.strategy.visitAndIncr(visitInfo);
//	}
	
}
