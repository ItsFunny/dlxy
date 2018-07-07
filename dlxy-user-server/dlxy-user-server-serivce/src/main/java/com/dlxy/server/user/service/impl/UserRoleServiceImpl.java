/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月7日 上午10:54:44
* 
*/
package com.dlxy.server.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.common.dto.UserRoleDTO;
import com.dlxy.server.user.dao.mybatis.UserRoleMybatisDao;
import com.dlxy.server.user.service.IUserRoleService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月7日 上午10:54:44
*/
@Service
public class UserRoleServiceImpl implements IUserRoleService
{
	@Autowired
	private UserRoleMybatisDao userRoleMybatisDao;

	@Override
	public UserRoleDTO findByRoleId(int roleId)
	{
		// TODO Auto-generated method stub
		return userRoleMybatisDao.findByRoleId(roleId);
	}

}
