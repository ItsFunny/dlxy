//package com.dlxy.listener;
//
//import java.util.concurrent.atomic.AtomicInteger;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import javax.servlet.http.HttpSessionEvent;
//import javax.servlet.http.HttpSessionListener;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.web.context.support.WebApplicationContextUtils;
//
//import com.dlxy.common.dto.VisitUserHistoryDTO;
//import com.dlxy.common.utils.JsonUtil;
//import com.dlxy.service.IRedisService;
//import com.joker.library.utils.CommonUtils;
//
//public class UserVisitListener implements HttpSessionListener,ServletContextListener
//{
//	
//	public static AtomicInteger onlineCount=new AtomicInteger(0);
//
//	@Autowired
//	private IRedisService redisService;
//	
//	private ApplicationContext applicationContext;
//	
//	@Override
//	public void sessionCreated(HttpSessionEvent se)
//	{
//		ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//		if(null==attributes) return;
//		HttpServletRequest request = attributes.getRequest();
//		HttpSession session = se.getSession();
//		try
//		{
//			String ip = CommonUtils.getIpAddr(request);
//			String userKey=String.format(IRedisService.ONLINE_USER_PREFIX+":%s",ip);
//			String userJson = redisService.get(userKey);
//			if(StringUtils.isEmpty(userJson))
//			{
//				onlineCount.incrementAndGet();
//				redisService.set(userKey, String.valueOf(System.currentTimeMillis()),60*5);
//			}
//			String json = redisService.get(String.format(IRedisService.USER_VISIT_HISTORY, ip));
//			if(!StringUtils.isEmpty(json))
//			{
//				VisitUserHistoryDTO historyDTO = JsonUtil.json2Object(json, VisitUserHistoryDTO.class);
//				session.setAttribute("history", historyDTO);
//			}
//		} catch (Exception e)
//		{
//		}
//	}
//
//	@Override
//	public void sessionDestroyed(HttpSessionEvent se)
//	{
//		ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//		if(null==attributes) return;
//		HttpServletRequest request = attributes.getRequest();
//		String ip=CommonUtils.getIpAddr(request);
//		redisService.del(String.format(IRedisService.ONLINE_USER_PREFIX+":%s", ip));
//		onlineCount.decrementAndGet();
//	}
//
//	@Override
//	public void contextInitialized(ServletContextEvent sce)
//	{
//		ServletContext servletContext = sce.getServletContext();
//		this.applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
//		this.redisService=applicationContext.getBean(IRedisService.class);
//	}
//
//	@Override
//	public void contextDestroyed(ServletContextEvent sce)
//	{
//		
//	}
//
//}
