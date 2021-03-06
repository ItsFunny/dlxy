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
import java.util.List;
import java.util.Map;

import com.dlxy.common.dto.UserDTO;
import com.dlxy.server.user.model.DlxyUser;
import com.dlxy.server.user.model.DlxyUserExample;

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
	/*
	 * 第一个方法可以cancel 了
	 */
	UserDTO findUserByNameOrId(String key) throws SQLException;
	
	UserDTO findByUserId(Long userId) throws SQLException;
	
	UserDTO findByUsername(String username);
	
	Integer deleteUser(List<Long> userIds);
	
	Integer deleteUseByUserId(Long userId);
	
	void updateUserStatusByUserId(Long userId,Integer status);
	
	int updateUserByUserId(DlxyUser user);
	
	void updateUserByExample(DlxyUser user);
	
	Long countUsersByParam(Map<String, Object>params) throws SQLException;
	
	Collection<UserDTO>findUsersByPage(int start,int end ,Map<String, Object>params) throws SQLException;
	
	Long addUser(UserDTO userDTO) throws SQLException;
}
