/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月26日 上午10:55:55
* 
*/
package com.dlxy.service;

import java.util.Map;


import com.dlxy.common.utils.JsonUtil;
import com.dlxy.model.ArticleVisitInfo;
import com.dlxy.server.article.service.IArticleService;

public class RedisArticleVIsitCountStrategy extends AbstractArticleVistitCountStrategy
{
	private IRedisService redisService;

	private IArticleService articleService;

	public RedisArticleVIsitCountStrategy(IRedisService redisService, IArticleService articleService)
	{
		this.redisService = redisService;
		this.articleService = articleService;
	}
	// @Override
	// public Integer visitAndIncr(Long articleId)
	// {
	// String key = String.format(IRedisService.ARTICLE_VISIT_COUNT, articleId);
	// String json = redisService.get(key);
	//
	// return null;
	// }
	//
	// @Override
	// protected ArticleVisitInfo getVisiterInfo(Long articleId)
	// {
	// ArticleVisitInfo info=null;
	// String key = String.format(IRedisService.ARTICLE_VISIT_COUNT, articleId);
	// String json = redisService.get(key);
	// if(StringUtils.isEmpty(json))
	// {
	// ArticleDTO articleDTO = articleService.findByArticleId(articleId);
	// if(null==articleDTO)
	// {
	// throw new RuntimeException("文章不存在");
	// }
	// info=ArticleVisitCountFactory.create(articleDTO.getVisitCount());
	// }else {
	// info=ArticleVisitCountFactory.createFromJson(json);
	// }
	// return info;
	// }

	@Override
	protected Integer parse(ArticleVisitInfo visitInfo, String ip)
	{
		Map<String, Long> visitors = visitInfo.getVisitors();
		visitors.put(ip, System.currentTimeMillis());
		visitInfo.incr();
		String key = String.format(IRedisService.ARTICLE_VISIT_COUNT, visitInfo.getArticleId());
		redisService.set(key, JsonUtil.obj2Json(visitInfo));
		return visitInfo.get();
	}

}
