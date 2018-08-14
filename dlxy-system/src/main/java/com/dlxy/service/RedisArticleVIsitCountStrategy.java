package com.dlxy.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dlxy.common.event.AppEvent;
import com.dlxy.common.event.AppEventPublisher;
import com.dlxy.common.event.Events;
import com.dlxy.common.model.ArticleVisitInfo;
import com.dlxy.common.utils.JsonUtil;
import com.dlxy.server.article.service.IArticleService;

import redis.clients.jedis.exceptions.JedisException;

public class RedisArticleVIsitCountStrategy extends AbstractArticleVistitCountStrategy
{
	private Logger logger = LoggerFactory.getLogger(RedisArticleVIsitCountStrategy.class);

	private IRedisService redisService;

	private IArticleService articleService;

	private AppEventPublisher appEventPublisher;

	public RedisArticleVIsitCountStrategy(IRedisService redisService, IArticleService articleService,
			AppEventPublisher eventPublisher)
	{
		this.redisService = redisService;
		this.articleService = articleService;
		this.appEventPublisher = eventPublisher;
	}

	@Override
	protected Integer parse(ArticleVisitInfo visitInfo, String ip)
	{
		Map<String, Long> visitors = visitInfo.getVisitors();
		visitors.put(ip, System.currentTimeMillis());
		visitInfo.incr();
		String key = String.format(IRedisService.ARTICLE_VISIT_COUNT, visitInfo.getArticleId());
		try
		{
			redisService.set(key, JsonUtil.obj2Json(visitInfo),60*60*24*5);
			redisService.zAdd(IRedisService.ARTICLE_VISIT_RANGE, (double)visitInfo.getVisitCount().get(), visitInfo.getArticleId().toString());
		} catch (Exception e)
		{
			throw new JedisException(e);
		}
		try
		{
			HashMap<String, Object> map = new HashMap<>();
			map.put("articleId", visitInfo.getArticleId());
			appEventPublisher.publish(new AppEvent(map, Events.ArticleVisitCount.name()));
		} catch (Exception e)
		{
			// 发送邮箱提示rabbitMQ服务挂了
			logger.error("[获取文章访问记录]rabbitMQ服务器挂了,error:{}", e.getMessage());
		}
		return visitInfo.get();
	}

	public static void main(String[] args)
	{
		System.out.println(test());
	}

	public static Integer test()
	{
		System.out.println("aaa");
		try
		{
			System.out.println(1 / 0);
		} catch (Exception e)
		{
			throw e;
		}
		return 1;
	}

}
