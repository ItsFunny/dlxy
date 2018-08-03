package com.dlxy.service;


import java.util.concurrent.atomic.AtomicInteger;

import com.dlxy.model.ArticleVisitInfo;
import com.dlxy.server.article.service.IArticleService;

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
