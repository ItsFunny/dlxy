/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午4:14:40
* 
*/
package com.dlxy.system.batch.consumer;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dlxy.common.event.AmqpListener;
import com.dlxy.common.event.AppEvent;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月29日 下午4:14:40
*/
public class UserRecordListener implements AmqpListener
{
	private Logger logger=LoggerFactory.getLogger(UserRecordListener.class);
	@Override
	public String getQueueName()
	{
		return "UserRecordLog";
	}

	@Override
	public void process(AppEvent event)
	{
		logger.info("[user_record] begin consume the message");
		HashMap<String, Object>data=(HashMap<String, Object>) event.getData();
		logger.info("[user_record] the data is {} and {}",data.get("userId"),data.get("detail"));
	}

}
