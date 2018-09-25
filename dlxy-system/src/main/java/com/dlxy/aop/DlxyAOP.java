package com.dlxy.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dlxy.common.dto.SuspicionDTO;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.exception.DlxySuspicionException;
import com.dlxy.service.IRedisService;
import com.joker.library.utils.CommonUtils;

@Aspect
@Component
@Order(1)
public class DlxyAOP
{
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IRedisService redisService;

	@Pointcut("@annotation (com.dlxy.annotation.CheckIllegalFormat)")
	public void checkIsIllegalFormat()
	{
	}
	
	@Pointcut("execution (* com.dlxy.controller.PortalController.*(..)) ")
	public void extendUserPortalExpireInRedis() {}
	
	@Pointcut("execution (* com.dlxy.controller.AdminController.*(..)) && ((!execution (* com.dlxy.controller.AdminController.login(..))) && (!execution (* com.dlxy.controller.AdminController.doLogin(..))))")
	public void extendUserManagerExpireInRedis() {}
	
	@Pointcut("extendUserManagerExpireInRedis()  || extendUserPortalExpireInRedis()")
	public void extendUserExpireInRedis()
	{
	}
	@After("extendUserExpireInRedis()")
	public void extendRedisExpire()
	{
		try
		{
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			String ip = (String) request.getSession().getAttribute("ip");
			redisService.expire(String.format(IRedisService.ONLINE_USER_PREFIX + ":%s", ip), 60 * 12);
		} catch (Exception e)
		{
			
			logger.error("Redis服务器挂了,{}", e.getMessage());
		}

	}

	@Before("checkIsIllegalFormat()")
	public void checkIsIllegal(JoinPoint joinPoint)
	{
		Object[] args = joinPoint.getArgs();
		if (null != args)
		{
			try
			{
				String idStr = (String) args[0];
				Long.parseLong(idStr);
			} catch (NumberFormatException e)
			{
				ServletRequestAttributes aatAttributes = (ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes();
				HttpServletRequest request = aatAttributes.getRequest();
				SuspicionDTO suspicionDTO = new SuspicionDTO(CommonUtils.getRemortIP(request),
						IllegalLevelEnum.Suspicious.ordinal(), request.getRequestURI());
				throw new DlxySuspicionException("用户试图访问不存在的记录", suspicionDTO);
			}
		}
	}
}
