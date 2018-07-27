/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月22日 下午1:15:51
* 
*/
package com.dlxy.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dlxy.common.dto.SuspicionDTO;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.exception.DlxySuspicionException;
import com.joker.library.utils.CommonUtils;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月22日 下午1:15:51
*/
@Aspect
@Component
@Order(1)
public class IllegalFormatAOP
{
	@Pointcut("@annotation (com.dlxy.annotation.CheckIllegalFormat)")
	public void checkIsIllegalFormat()
	{
	}@Before("checkIsIllegalFormat()")
	public void checkIsIllegal(JoinPoint joinPoint)
	{
		Object[] args = joinPoint.getArgs();
		if(null!=args)
		{
			try
			{
				String idStr=(String) args[0];
				Long.parseLong(idStr);
			} catch (NumberFormatException e)
			{
				ServletRequestAttributes aatAttributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				HttpServletRequest request = aatAttributes.getRequest();
				SuspicionDTO suspicionDTO=new SuspicionDTO(CommonUtils.getRemortIP(request), IllegalLevelEnum.Suspicious.ordinal(), request.getRequestURI());
				throw new DlxySuspicionException("用户试图访问不存在的记录", suspicionDTO);
			}
		}
	}
}
