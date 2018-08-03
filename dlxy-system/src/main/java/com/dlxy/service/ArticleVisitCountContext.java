package com.dlxy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dlxy.model.ArticleVisitCountFactory;
import com.dlxy.model.ArticleVisitInfo;
import com.dlxy.server.article.service.IArticleService;

@Component
public class ArticleVisitCountContext
{
	private AbstractArticleVistitCountStrategy strategy;

	@Autowired
	private IArticleService articleService;
	@Autowired
	private IRedisService redisService;

	public Integer visitCount(Long articleId)
	{
		ArticleVisitInfo visitInfo = ArticleVisitCountFactory.get(articleId, redisService, articleService);
		if (redisService.isAvaliable())
		{
			this.strategy = new RedisArticleVIsitCountStrategy(redisService, articleService);
		} else
		{
			this.strategy = new DbVisitCountStrategy(articleService);
		}
		return this.strategy.visitAndIncr(visitInfo);
	}
}
