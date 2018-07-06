/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午7:00:55
* 
*/
package com.dlxy.server.user.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import com.dlxy.common.dto.UserDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月29日 下午7:00:55
*/
public interface IUserService
{
	UserDTO findUserByNameOrId(String key) throws SQLException;
	
	UserDTO findByUserId(Long userId) throws SQLException;
	
	
	Long countUsersByParam(Map<String, Object>params) throws SQLException;
	
	Collection<UserDTO>findUsersByPage(int start,int end ,Map<String, Object>params) throws SQLException;
	
	Long addUser(UserDTO userDTO) throws SQLException;
}
