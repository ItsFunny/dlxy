/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午7:04:32
* 
*/
package com.dlxy.server.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.server.user.dao.mybatis.UserRecordMybatisDao;
import com.dlxy.server.user.service.IUserRecordService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月29日 下午7:04:32
*/
@Service
public class UserRecordServiceImpl implements IUserRecordService
{
	
	@Autowired
	private UserRecordMybatisDao userRecordMybatisDao;
	
	public void addRecord(UserRecordDTO userRecordDTO)
	{
		userRecordMybatisDao.addRecord(userRecordDTO);
	}

}
