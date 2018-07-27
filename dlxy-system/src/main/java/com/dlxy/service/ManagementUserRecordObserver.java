/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 上午9:54:45
* 
*/
package com.dlxy.service;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.server.user.service.IUserRecordService;
import com.dlxy.task.DlxyTaskRunner;
import com.dlxy.task.DlxyThreadTask;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月2日 上午9:54:45
*/
public class ManagementUserRecordObserver implements Observer
{
	Logger logger=LoggerFactory.getLogger(ManagementUserRecordObserver.class);
	
	@Autowired
	private IUserRecordService userRecordService;
	
	private class UserRecordRunnable extends DlxyTaskRunner
	{
		UserRecordRunnable(UserRecordDTO userRecordDTO)
		{
			super(userRecordDTO);
		}
		@Override
		public void run()
		{
			logger.info("[UserRecordRunnable] get message");
			try
			{
				userRecordService.addRecord((UserRecordDTO)this.data);
			} catch (Exception e)
			{
				logger.info("[UserRecordRunnable] occur error :{}",e.getMessage());
				//TODO
			}
		}
		
	}
	@Override
	public void update(Observable o, Object obj)
	{
		
		if(obj instanceof UserRecordDTO)
		{
			UserRecordDTO recordDTO=(UserRecordDTO)obj;
			recordDTO.valid();
			logger.info("[observer]user_record_observer get message {}",obj);
			DlxyThreadTask.getTaskStorage().addJob(new UserRecordRunnable(recordDTO));
//			ExecutorService executorService = Executors.newFixedThreadPool(1);
//			executorService.execute(()->{
//				userRecordService.addRecord(recordDTO);
//				logger.info("[observer]user_record_observer finish the job");
//			});
		}
	}

}
