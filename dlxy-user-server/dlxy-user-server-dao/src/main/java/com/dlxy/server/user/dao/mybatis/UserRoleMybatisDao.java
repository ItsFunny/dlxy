package com.dlxy.server.user.dao.mybatis;

import java.util.Collection;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.dlxy.common.dto.UserRoleDTO;

@Mapper
public interface UserRoleMybatisDao
{
	@Select("select role_id,role_name,role_description from dlxy_role where role_id = #{roleId}")
	UserRoleDTO findByRoleId(int roleId);
	
	
	@Select("select role_id,role_name,role_description from dlxy_role")
	Collection<UserRoleDTO>findAllRoels();
	
	
}
