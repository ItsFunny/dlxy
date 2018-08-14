package com.dlxy.listener;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dlxy.common.dto.VisitUserHistoryDTO;
import com.dlxy.common.utils.JsonUtil;
import com.dlxy.service.IRedisService;
import com.joker.library.utils.CommonUtils;

public class ShiroSessionListener extends SessionListenerAdapter
{
	@Autowired
	private IRedisService redisService;
	
	public static AtomicInteger onlineCount=new AtomicInteger(0);
	
	@Override
	public void onStart(Session session)
	{
		ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(null==attributes) return;
		HttpServletRequest request = attributes.getRequest();
		try
		{
			String ip = CommonUtils.getIpAddr(request);
			String userKey=String.format(IRedisService.ONLINE_USER_PREFIX+":%s",ip);
			String userJson = redisService.get(userKey);
			if(StringUtils.isEmpty(userJson))
			{
				ShiroSessionListener.onlineCount.incrementAndGet();
				redisService.set(userKey, String.valueOf(System.currentTimeMillis()),60*5);
			}
			String json = redisService.get(String.format(IRedisService.USER_VISIT_HISTORY, ip));
			if(!StringUtils.isEmpty(json))
			{
				VisitUserHistoryDTO historyDTO = JsonUtil.json2Object(json, VisitUserHistoryDTO.class);
				session.setAttribute("history", historyDTO);
			}
		} catch (Exception e)
		{
		}
	}

	@Override
	public void onStop(Session session)
	{
		ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(null==attributes)return;
		HttpServletRequest request = attributes.getRequest();
		String ip=CommonUtils.getIpAddr(request);
		redisService.del(String.format(IRedisService.ONLINE_USER_PREFIX+":%s", ip));
		ShiroSessionListener.onlineCount.decrementAndGet();
	}

	@Override
	public void onExpiration(Session session)
	{
		ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(null==attributes)return;
		HttpServletRequest request = attributes.getRequest();
		String ip=CommonUtils.getIpAddr(request);
		redisService.del(String.format(IRedisService.ONLINE_USER_PREFIX+":%s", ip));
		ShiroSessionListener.onlineCount.decrementAndGet();
	}
}
