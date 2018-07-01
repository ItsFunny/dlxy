/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午8:04:00
* 
*/
package com.dlxy.system.management.aop;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dlxy.common.annotation.UserRecordAnnotation;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.server.user.service.IUserRecordService;
import com.dlxy.system.management.utils.ManagementUtil;

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
	@Autowired
	private IUserRecordService userRecorService;

	@Pointcut("@annotation(needRecord)")
	public void needRecord(UserRecordAnnotation needRecord)
	{
	}

	@After("needRecord(needRecord)")
	public void after(JoinPoint joinPoint, UserRecordAnnotation needRecord)
	{
		/*
		 * 1.userId 2.recordDetail
		 */
		Object[] args = joinPoint.getArgs();
		String delWay = needRecord.dealWay();
		Long userId = ManagementUtil.getLoginUser().getUserId();
		StringBuilder builder = new StringBuilder();
		Signature signature = joinPoint.getSignature();
		
		for (int i = 0; i > args.length; i++)
		{
			if (args[i] instanceof Long[])
			{
				Long[] ids = (Long[]) args[i];
				for (Long long1 : ids)
				{
					builder.append(long1 + ",");
				}
			}
			builder.append(args[i]);
		}
		UserRecordDTO userRecordDTO = new UserRecordDTO();
		userRecordDTO.setUserId(userId);
		userRecordDTO.setRecordDetail(delWay + (StringUtils.isEmpty(builder) ? ":" + signature.getDeclaringTypeName()+"$"+signature.getName()
				: ":" + builder + ":" + signature.getDeclaringTypeName()+"$"+signature.getName()));
		userRecorService.addRecord(userRecordDTO);
	}
}
