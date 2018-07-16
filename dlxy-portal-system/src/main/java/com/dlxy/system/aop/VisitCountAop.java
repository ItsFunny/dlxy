/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月9日 下午9:09:45
* 
*/
package com.dlxy.system.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.druid.util.StringUtils;
import com.dlxy.common.dto.VisitUserDTO;
import com.dlxy.common.exception.UnfindPageException;
import com.dlxy.common.utils.JsonUtil;
import com.dlxy.system.constants.RedisConstant;
import com.dlxy.system.constants.SessionConstant;
import com.dlxy.system.constants.VisitRecordConstant;
import com.dlxy.system.service.RedisService;
import com.google.gson.reflect.TypeToken;
import com.joker.library.utils.CommonUtils;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月9日 下午9:09:45
*/
@Aspect
public class VisitCountAop
{
	@Pointcut("execution ( * com.dlxy.system.controller.ArticleController.articleDetail.(..))")
	public void beginRecord() {}
	
	@Autowired
	private RedisService redisService;
	@Before("beginRecord()")
	public void filterUsers(ProceedingJoinPoint joinPoint)
	{
		Object[] args = joinPoint.getArgs();
		Long articleId=null;
		try
		{
			articleId=Long.parseLong(args[0].toString());
		} catch (Exception e)
		{
			throw new UnfindPageException();
		}
		
		ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String ip = CommonUtils.getRemortIP(request);
		String json = redisService.get(String.format(RedisConstant.SESSION_IP, ip));
		VisitUserDTO visitUserDTO=new VisitUserDTO();
		if(!StringUtils.isEmpty(json))
		{
			visitUserDTO=JsonUtil.json2Object(json,VisitUserDTO.class);
		}
		boolean isPassInterval = isPassInterval(visitUserDTO, articleId);
	}
	private boolean isPassInterval(VisitUserDTO visitUserDTO,Long articleId)
	{
		Long lastVisitDate = visitUserDTO.getLastVisitDate(articleId);
		if(null==lastVisitDate)
		{
			return true;
		}
		if((System.currentTimeMillis()-lastVisitDate)>VisitRecordConstant.VISIT_AGAIN_INTERVAL)
		{
			return true;
		}
		return false;
	}
}
