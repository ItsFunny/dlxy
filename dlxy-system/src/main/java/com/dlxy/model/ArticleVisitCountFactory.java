/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月22日 下午1:51:08
* 
*/
package com.dlxy.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.utils.JsonUtil;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.service.IRedisService;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月22日 下午1:51:08
 */
public class ArticleVisitCountFactory
{
	private static Map<Long, ArticleVisitInfo> articleStorage = new HashMap<>();

//	private   IRedisService redisService;
//	private   IArticleService articleService;

	// 这里最好是IRedisService 继承与某个接口好,因为可能会用mongodb做缓存,但暂时直接这样既可
	// public ArticleVisitCountFactory(IRedisService redisService)
	// {
	// this.redisService=redisService;
	// }
//	public  void init(IRedisService redisService,IArticleService articleService)
//	{
//		ArticleVisitCountFactory.redisService=redisService;
//		ArticleVisitCountFactory.articleService=articleService;
//	}

//	private  ArticleVisitInfo articleVisitInfo = new ArticleVisitInfo();

	public  static synchronized ArticleVisitInfo get(Long articleId,IRedisService redisService,IArticleService articleService)
	{
		ArticleVisitInfo visitInfo = null;
		if (articleStorage.containsKey(articleId))
		{
			visitInfo = articleStorage.get(articleId);
			return visitInfo;
		}
		String key = String.format(IRedisService.ARTICLE_VISIT_COUNT, articleId);
		String json =null;
		try
		{
			json= redisService.get(key);
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
		articleStorage.put(articleId, visitInfo);
		return visitInfo;
	}

//	public  synchronized ArticleVisitInfo create(Integer visitCount, Map<String, Long> visitors)
//	{
//		articleVisitInfo.setVisitCount(visitCount);
//		articleVisitInfo.setVisitors(visitors);
//		return articleVisitInfo;
//	}
//
//	public  synchronized ArticleVisitInfo create(Integer visitCount)
//	{
//		articleVisitInfo.setVisitCount(visitCount);
//		articleVisitInfo.setVisitors(new HashMap<String, Long>());
//		return articleVisitInfo;
//	}
//
//	public  synchronized ArticleVisitInfo createFromJson(String json)
//	{
//		articleVisitInfo = JsonUtil.json2Object(json, ArticleVisitInfo.class);
//		return articleVisitInfo;
//	}

}
