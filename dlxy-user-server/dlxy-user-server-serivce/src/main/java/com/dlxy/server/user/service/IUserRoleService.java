/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月7日 上午10:54:19
* 
*/
package com.dlxy.server.user.service;

import java.util.Collection;

import com.dlxy.common.dto.UserRoleDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月7日 上午10:54:19
*/
public interface IUserRoleService
{
	UserRoleDTO findByRoleId(int roleId);
	
	
	
	Collection<UserRoleDTO>findAllRoles();
}
