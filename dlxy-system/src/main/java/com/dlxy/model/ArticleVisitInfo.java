/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月22日 下午1:48:38
* 
*/
package com.dlxy.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月22日 下午1:48:38
*/
public class ArticleVisitInfo 
{
	private Long articleId;
	private AtomicInteger visitCount;
	private Map<String, Long> visitors;

	
	public ArticleVisitInfo()
	{
		this.visitors=new HashMap<>();
	}
	public Integer get()
	{
		return this.visitCount.get();
	}
	public Integer incr()
	{
		return visitCount.incrementAndGet();
	}

	public void setVisitCount(Integer visitCount)
	{
		this.visitCount=new AtomicInteger(visitCount);
	}
	public  Map<String, Long> getVisitors()
	{
		return visitors;
	}
	public void setVisitors(Map<String, Long> visitors)
	{
		this.visitors = visitors;
	}
	public Long getArticleId()
	{
		return articleId;
	}
	public void setArticleId(Long articleId)
	{
		this.articleId = articleId;
	}
	public AtomicInteger getVisitCount()
	{
		return visitCount;
	}
	public void setVisitCount(AtomicInteger visitCount)
	{
		this.visitCount = visitCount;
	}

	
	

	
	
	
}
