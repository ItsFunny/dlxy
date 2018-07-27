/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月26日 上午10:41:59
* 
*/
package com.dlxy.service;


import java.util.concurrent.atomic.AtomicInteger;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.model.ArticleVisitInfo;
import com.dlxy.server.article.service.IArticleService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月26日 上午10:41:59
*/
public class DbVisitCountStrategy extends AbstractArticleVistitCountStrategy
{
	
	
	private IArticleService articleService;
	
	public DbVisitCountStrategy(IArticleService articleService)
	{
		this.articleService=articleService;
	}


	@Override
	protected Integer parse(ArticleVisitInfo articleVisitInfo,String ip)
	{
		AtomicInteger visitCount=articleVisitInfo.getVisitCount();
		int count = visitCount.incrementAndGet();
		articleService.updateArticleVisitCount(articleVisitInfo.getArticleId(), count);
		return count;
	}
	
	
	

}
