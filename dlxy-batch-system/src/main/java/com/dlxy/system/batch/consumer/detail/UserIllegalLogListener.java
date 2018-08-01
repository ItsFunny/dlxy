/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 上午8:08:07
* 
*/
package com.dlxy.system.batch.consumer.detail;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;

import java.security.interfaces.RSAMultiPrimePrivateCrtKey;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.event.AmqpListener;
import com.dlxy.common.event.AppEvent;
import com.dlxy.common.event.Events;
import com.dlxy.common.utils.JsonUtil;
import com.dlxy.server.user.model.DlxyUserIllegalLog;
import com.dlxy.server.user.service.IUserIllegalLogService;
import com.dlxy.system.batch.service.IRedisService;

/**
* 
* @When
* @Description 这里的业务有点牵强了
* @Detail
* @author joker 
* @date 创建时间：2018年7月1日 上午8:08:07
*/
public class UserIllegalLogListener implements AmqpListener
{
	private Logger logger=LoggerFactory.getLogger(UserIllegalLogListener.class);
	@Autowired
	private IUserIllegalLogService userIllegalLogService;
	
	@Autowired
	private QueryRunner queryRunner;
	
	@Autowired
	private IRedisService redisService;
	
	private Integer illegalLimitCount=0;
	
	@Override
	public String getQueueName()
	{
		return Events.UserIllegalLog.name();
	}

	@Override
	public void process(AppEvent event)
	{
		IllegalLogDTO data=(IllegalLogDTO) event.getData();
//		HashMap<String, Object>data=(HashMap<String, Object>) event.getData();
		if(null==data)
		{
			logger.warn("no data found from event {}",event);
			return;
		}
		try
		{
			String detail=data.getIllegalDetail();
			if(StringUtils.isEmpty(detail))
			{
				logger.warn("[user_illegal_log_listener] no detail found from event {}",event);
				return;
			}
			DlxyUserIllegalLog illegalLog=new DlxyUserIllegalLog();
			BeanUtils.copyProperties(illegalLog, data);
			userIllegalLogService.addIllegalLog(illegalLog);
			Set<String> ips = getBannedIps();
			if(ips!=null)
			{
				String json=JsonUtil.obj2Json(ips);
				redisService.set(IRedisService.BANED_IP, json);
				redisService.expire(IRedisService.BANED_IP, 60*60*60*24);
			}
		} catch (Exception e)
		{
			logger.error("[user_illegal_log_listener] error {}",e.getMessage());
		}
	}
	
	private Set<String>getBannedIps()
	{
		Set<String>ips=new HashSet<String>();
		String sql="select COUNT(1),ip from dlxy_user_illegal_log GROUP BY ip having count(1)> 0 ";
		try
		{
			List<Map<String, Object>> list = queryRunner.query(sql, new MapListHandler());
			if(null!=list && !list.isEmpty())
			{
				for (Map<String, Object> map : list)
				{
					ips.add(map.get("ip").toString());
				}
			}else {
				return null;
			}
		} catch (SQLException e)
		{
			logger.error("[IllegalConsumer] occer sql error:{}",e.getMessage());
			e.printStackTrace();
		}
		return ips;
	}

}
