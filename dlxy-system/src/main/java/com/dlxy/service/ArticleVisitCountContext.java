package com.dlxy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dlxy.common.event.AppEventPublisher;
import com.dlxy.model.ArticleVisitCountFactory;
import com.dlxy.model.ArticleVisitInfo;
import com.dlxy.server.article.service.IArticleService;

import redis.clients.jedis.exceptions.JedisException;

@Component
public class ArticleVisitCountContext
{
	private AbstractArticleVistitCountStrategy strategy;

	@Autowired
	private IArticleService articleService;
	@Autowired
	private IRedisService redisService;
	
	@Autowired
	private AppEventPublisher eventPublisher;

	public Integer visitCount(Long articleId)
	{
		ArticleVisitInfo visitInfo = ArticleVisitCountFactory.get(articleId, redisService, articleService);
		if (redisService.isAvaliable())
		{
			this.strategy = new RedisArticleVIsitCountStrategy(redisService, articleService,eventPublisher);
		} else
		{
			this.strategy = new DbVisitCountStrategy(articleService);
		}
		Integer res =null;
		try
		{
			res=this.strategy.visitAndIncr(visitInfo);
		} catch (JedisException e)
		{
			this.strategy=new DbVisitCountStrategy(articleService);
			res=this.strategy.visitAndIncr(visitInfo);
		}
		return res;
	}
}
