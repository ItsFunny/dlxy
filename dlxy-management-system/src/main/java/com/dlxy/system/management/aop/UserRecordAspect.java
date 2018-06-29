/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午8:04:00
* 
*/
package com.dlxy.system.management.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月29日 下午8:04:00
*/
@Aspect
@Component
public class UserRecordAspect
{
	@Pointcut("@annotation(com.dlxy.common.annotation.NeedRecord)")
	public void needRecord() {}
	
	@Before("needRecord()")
	public void before(JoinPoint joinPoint)
	{
		Object[] args = joinPoint.getArgs();
		for (Object object : args)
		{
			System.out.println(object);
		}
		System.out.println("this is before advice");
	}
}
