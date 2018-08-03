package com.dlxy.service;

import java.util.Map;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dlxy.model.ArticleVisitInfo;
import com.joker.library.utils.CommonUtils;

public abstract class AbstractArticleVistitCountStrategy
{

	public Integer visitAndIncr(ArticleVisitInfo visitInfo)
	{
		// ArticleVisitInfo visitInfo = getVisiterInfo(articleId);
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		String ip = CommonUtils.getRemortIP(attributes.getRequest());
		synchronized (visitInfo)
		{
			if (filter(visitInfo, ip))
			{
				Integer res = parse(visitInfo, ip);
				addVisitor(visitInfo, ip);
				return res;
			} else
			{
				return visitInfo.get();
			}
		}
	}

	private boolean filter(ArticleVisitInfo visitInfo, String ip)
	{
		Map<String, Long> visitors = visitInfo.getVisitors();
		if (null != visitors)
		{
			Long lastVisitTime = visitors.get(ip);
			if (null != lastVisitTime && System.currentTimeMillis() - lastVisitTime < 1000 * 90)
			{
				return false;
			} else
			{
				return true;
			}
		} else
		{
			return true;
		}
	}

	private void addVisitor(ArticleVisitInfo visitInfo, String ip)
	{
		visitInfo.getVisitors().put(ip, System.currentTimeMillis());
	}

	protected abstract Integer parse(ArticleVisitInfo articleVisitInfo, String ip);
}
