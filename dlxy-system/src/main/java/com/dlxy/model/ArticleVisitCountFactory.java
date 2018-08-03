package com.dlxy.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.utils.JsonUtil;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.service.IRedisService;

public class ArticleVisitCountFactory
{
	private static Map<Long, ArticleVisitInfo> ARTICLESTORAGE = new HashMap<>();

	public static synchronized ArticleVisitInfo get(Long articleId, IRedisService redisService,
			IArticleService articleService)
	{
		ArticleVisitInfo visitInfo = null;
		if (ARTICLESTORAGE.containsKey(articleId))
		{
			visitInfo = ARTICLESTORAGE.get(articleId);
			return visitInfo;
		}
		String key = String.format(IRedisService.ARTICLE_VISIT_COUNT, articleId);
		String json = null;
		try
		{
			json = redisService.get(key);
		} catch (Exception e)
		{
		}
		if (StringUtils.isEmpty(json))
		{
			ArticleDTO articleDTO = articleService.findByArticleId(articleId);
			if (null == articleDTO)
			{
				throw new RuntimeException("文章不存在");
			} else
			{
				visitInfo = new ArticleVisitInfo();
				visitInfo.setArticleId(articleId);
				visitInfo.setVisitCount(articleDTO.getVisitCount());
			}
		} else
		{
			visitInfo = JsonUtil.json2Object(json, ArticleVisitInfo.class);
		}
		ARTICLESTORAGE.put(articleId, visitInfo);
		return visitInfo;
	}

}
