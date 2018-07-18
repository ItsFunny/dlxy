/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月7日 上午10:50:12
* 
*/
package com.dlxy.server.user.dao.mybatis;

import java.util.Collection;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.dlxy.common.dto.UserRoleDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月7日 上午10:50:12
*/
@Mapper
public interface UserRoleMybatisDao
{
	@Select("select role_id,role_name,role_description from dlxy_role where role_id = #{roleId}")
	UserRoleDTO findByRoleId(int roleId);
	
	
	@Select("select role_id,role_name,role_description from dlxy_role")
	Collection<UserRoleDTO>findAllRoels();
	
	
}
