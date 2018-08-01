/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 上午8:17:06
* 
*/
package com.dlxy.server.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.server.user.dao.mybatis.UserIllegalLogDao;
import com.dlxy.server.user.model.DlxyUserIllegalLog;
import com.dlxy.server.user.service.IUserIllegalLogService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月1日 上午8:17:06
*/
@Service
public class UserIllegalLogServiceImpl implements IUserIllegalLogService
{
	
	@Autowired
	private UserIllegalLogDao userIllegalLogDao;
	@Override
	public void addIllegalLog(DlxyUserIllegalLog illeglog)
	{
		userIllegalLogDao.insertSelective(illeglog);
	}
}
