package com.dlxy.listener;

import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dlxy.common.dto.VisitUserHistoryDTO;
import com.dlxy.common.utils.JsonUtil;
import com.dlxy.service.IRedisService;
import com.joker.library.utils.CommonUtils;

public class UserVisitListener implements HttpSessionListener,ServletContextListener
{
	
	public static AtomicInteger onlineCount=new AtomicInteger(0);
	
	public static Vector<String>errorIps=new Vector<>();

	private IRedisService redisService;
	
	private ApplicationContext applicationContext;
	
	@Override
	public void sessionCreated(HttpSessionEvent se)
	{
		ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(null==attributes) return;
		HttpServletRequest request = attributes.getRequest();
		HttpSession session = se.getSession();
		try
		{
			String ip = CommonUtils.getIpAddr(request);
			String userKey=String.format(IRedisService.ONLINE_USER_PREFIX+":%s",ip);
			session.setAttribute("ip", ip);
			String userJson = redisService.get(userKey);
			if(StringUtils.isEmpty(userJson))
			{
				UserVisitListener.onlineCount.incrementAndGet();
				redisService.set(userKey, System.currentTimeMillis()+"",60*12);
			}
			String json = redisService.get(String.format(IRedisService.USER_VISIT_HISTORY, ip));
			VisitUserHistoryDTO historyDTO =null;
			if(!StringUtils.isEmpty(json))
			{
				historyDTO = JsonUtil.json2Object(json, VisitUserHistoryDTO.class);
			}else {
				historyDTO=new VisitUserHistoryDTO();
			}
			historyDTO.setIp(ip);
			session.setAttribute("history", historyDTO);
		} catch (Exception e)
		{
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se)
	{
		try
		{
			HttpSession session = se.getSession();
			String ip=(String) session.getAttribute("ip");
			String ipKey=String.format(IRedisService.ONLINE_USER_PREFIX+":%s", ip);
			String json = redisService.get(ipKey);
			if(!StringUtils.isEmpty(json))
			{
				
				Boolean del = redisService.del(ipKey);
				if(del)
				{
					UserVisitListener.onlineCount.decrementAndGet();
				}else {
					errorIps.add(ip);//待处理
					redisService.set(ipKey, System.currentTimeMillis()+"",-1);
				}
			}
		} finally
		{
			se=null;
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		ServletContext servletContext = sce.getServletContext();
		this.applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		this.redisService=applicationContext.getBean(IRedisService.class);
		Set<String> keys = redisService.getKeysByPrefix(IRedisService.ONLINE_USER_PREFIX);
		if(!keys.isEmpty())
		{
			for (String string : keys)
			{
				redisService.del(string);
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		
	}

}
