/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月9日 下午9:08:06
* 
*/
package com.dlxy.system.listener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dlxy.system.service.RedisService;
import com.joker.library.utils.CommonUtils;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月9日 下午9:08:06
*/
public class PortalVisiterListener implements HttpSessionListener
{

	@Autowired
	private RedisService redisService;
	@Override
	public void sessionCreated(HttpSessionEvent se)
	{
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se)
	{
		
	}

}
