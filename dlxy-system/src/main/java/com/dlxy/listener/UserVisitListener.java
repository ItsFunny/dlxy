/**
*
* @author joker 
* @date 创建时间：2018年8月7日 下午8:09:49
* 
*/
package com.dlxy.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dlxy.common.dto.VisitUserHistoryDTO;
import com.dlxy.common.utils.JsonUtil;
import com.dlxy.service.IRedisService;
import com.joker.library.utils.CommonUtils;

/**
* 
* @author joker 
* @date 创建时间：2018年8月7日 下午8:09:49
*/
public class UserVisitListener implements HttpSessionListener,ServletContextListener
{

	@Autowired
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
	public void sessionDestroyed(HttpSessionEvent se)
	{
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		ServletContext servletContext = sce.getServletContext();
		this.applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		this.redisService=applicationContext.getBean(IRedisService.class);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		
	}

}
