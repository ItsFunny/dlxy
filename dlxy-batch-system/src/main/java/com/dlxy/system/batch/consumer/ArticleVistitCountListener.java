/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月27日 下午3:00:39
* 
*/
package com.dlxy.system.batch.consumer;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dlxy.common.event.AmqpListener;
import com.dlxy.common.event.AppEvent;
import com.dlxy.system.batch.jobs.ArticleVisitCountJob;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月27日 下午3:00:39
*/
public class ArticleVistitCountListener implements AmqpListener
{
	private Logger logger=LoggerFactory.getLogger(ArticleVistitCountListener.class);
	
	@Override
	public String getQueueName()
	{
		return "ArticleVisitCount";
	}

	@Override
	public void process(AppEvent event)
	{
		HashMap<String, Object>data=(HashMap<String, Object>) event.getData();
		logger.info("[ArticleVistitCountListener] get the message {}",data);
		try
		{
			String articleId = data.get("articleId").toString();
			if(!ArticleVisitCountJob.ARTICLEIDS.contains(articleId))
			{
				ArticleVisitCountJob.ARTICLEIDS.put(data.get("articleId").toString());
			}
			logger.info("[ArticleVisitCountlistener]finish consume the message ");
		} catch (InterruptedException e)
		{
			logger.error("[ArticleVisitCountlistener] occur error {}",e.getMessage(),e);
		}
		
	}
}
