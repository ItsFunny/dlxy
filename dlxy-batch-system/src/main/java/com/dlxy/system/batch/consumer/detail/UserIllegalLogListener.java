/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 上午8:08:07
* 
*/
package com.dlxy.system.batch.consumer.detail;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.common.event.AmqpListener;
import com.dlxy.common.event.AppEvent;
import com.dlxy.common.event.Events;
import com.dlxy.server.user.service.IUserIllegalLogService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月1日 上午8:08:07
*/
public class UserIllegalLogListener implements AmqpListener
{
	private Logger logger=LoggerFactory.getLogger(UserIllegalLogListener.class);
	@Autowired
	private IUserIllegalLogService userIllegalLogService;
	@Override
	public String getQueueName()
	{
		return Events.UserIllegalLog.name();
	}

	@Override
	public void process(AppEvent event)
	{
		HashMap<String, Object>data=(HashMap<String, Object>) event.getData();
		if(null==data||data.isEmpty())
		{
			logger.warn("no data found from event {}",event);
			return;
		}
		try
		{
			String detail=(String) data.get("illegalDetail");
			if(StringUtils.isEmpty(detail))
			{
				logger.warn("[user_illegal_log_listener] no detail found from event {}",event);
				return;
			}
			Long userId=Long.parseLong(String.valueOf(data.get("userId")));
			Integer level=Integer.parseInt(String.valueOf(data.get("illegalLevel")));
			userIllegalLogService.addIllegalLog(userId, detail, level);
		} catch (Exception e)
		{
			logger.error("[user_illegal_log_listener] error {}",e.getMessage());
		}
	}

}
