/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月27日 上午10:56:49
* 
*/
package com.dlxy.task;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月27日 上午10:56:49
*/
public class DlxyThreadTask implements Runnable 
{
	private Logger logger=LoggerFactory.getLogger(DlxyThreadTask.class);
	
	private LinkedBlockingQueue<DlxyTaskRunner>details;
	
	private ExecutorService service;
	
	private static DlxyThreadTask taskRunner=new DlxyThreadTask();
	
	public static DlxyThreadTask  getTaskStorage()
	{
		return taskRunner;
	}
	
	public void addJob(DlxyTaskRunner runner)
	{
		try
		{
			logger.info("[ThreadTask] add job {}",runner);
			this.details.put(runner);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	private DlxyThreadTask()
	{
		this.service=Executors.newFixedThreadPool(10);
		this.details=new LinkedBlockingQueue<>();
		service.execute(this);
	}
	
	

	@Override
	public void run()
	{
		while(!Thread.currentThread().isInterrupted())
		{
			try
			{
				DlxyTaskRunner taskDetail = details.take();
				logger.info("ThreadTask begin consume  the  job :{}",taskDetail);
				service.execute(taskDetail);
			} catch (InterruptedException e)
			{
				logger.error("[ThreadTask] orrur error,interrupted {}",e.getMessage());
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
