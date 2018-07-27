/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月27日 下午3:07:25
* 
*/
package com.dlxy.system.batch.jobs;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月27日 下午3:07:25
*/
@Component
public class ArticleVisitCountJob implements JobRunner
{
	public static final LinkedBlockingQueue<String>ARTICLEIDS=new LinkedBlockingQueue<>();

	@Scheduled(cron="* * * * * ?")
	@Override
	public void run()
	{
		System.out.println("1111");
	}

}
