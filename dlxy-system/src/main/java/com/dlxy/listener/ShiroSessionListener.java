package com.dlxy.listener;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.apache.shiro.session.mgt.eis.SessionDAO;
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
	
	@Autowired
	private SessionDAO sessionDAO;
	
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
			session.setAttribute("ipKey", userKey);
			String userJson = redisService.get(userKey);
			if(StringUtils.isEmpty(userJson))
			{
				boolean isAlreadyOn=false;
				Collection<Session> visitUsers = sessionDAO.getActiveSessions();
				for (Session session2 : visitUsers)
				{
					String storeIp=(String) session2.getAttribute("ip");
					if(!StringUtils.isEmpty(storeIp)&&storeIp.equals(ip))
					{
						isAlreadyOn=true;
						break;
					}
				}
				if(!isAlreadyOn)
				{
					session.setAttribute("ip", ip);
					ShiroSessionListener.onlineCount.incrementAndGet();
				}else {
					sessionDAO.delete(session);
				}
			
//				redisService.set(userKey, String.valueOf(System.currentTimeMillis()),60*7);
			}
			String json = redisService.get(String.format(IRedisService.USER_VISIT_HISTORY, ip));
			if(!StringUtils.isEmpty(json))
			{
				VisitUserHistoryDTO historyDTO = JsonUtil.json2Object(json, VisitUserHistoryDTO.class);
				session.setAttribute("history", historyDTO);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onStop(Session session)
	{
		try
		{
			String ipKey = (String) session.getAttribute("ipKey");
//			redisService.del(ipKey);
			sessionDAO.delete(session);
			ShiroSessionListener.onlineCount.decrementAndGet();
		} finally
		{
			session=null;
		}
	}

	@Override
	public void onExpiration(Session session)
	{
		try
		{
			String ipKey = (String) session.getAttribute("ipKey");
//			redisService.del(ipKey);
			sessionDAO.delete(session);
			ShiroSessionListener.onlineCount.decrementAndGet();
		} finally
		{
			session=null;
		}
		
	}
}
