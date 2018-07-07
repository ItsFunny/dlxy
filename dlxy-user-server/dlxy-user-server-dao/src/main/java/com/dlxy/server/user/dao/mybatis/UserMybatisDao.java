/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月6日 上午9:23:01
* 
*/
package com.dlxy.server.user.dao.mybatis;

import java.util.Collection;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.dlxy.common.dto.UserDTO;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月6日 上午9:23:01
 */
@Mapper
public interface UserMybatisDao
{

	Long addUser(UserDTO userDTO);
	
	Collection<UserDTO>findUsersByPage(@Param("start")int start,@Param("end")int end ,Map<String, Object>params);
	
	@Select("select a.user_id,a.username,a.realname,a.password,a.role_id,a.last_login_ip,a.create_date,a.update_date from dlxy_user a where 1=1 and a.user_id=#{userId}")
	UserDTO findByUserId(Long userId);
	
	@Select("select a.user_id,a.username,a.realname,a.password,a.role_id,a.last_login_ip,a.create_date,a.update_date from dlxy_user a where 1=1 and a.username=#{username}")
	UserDTO findByUsername(String username);
	
}
