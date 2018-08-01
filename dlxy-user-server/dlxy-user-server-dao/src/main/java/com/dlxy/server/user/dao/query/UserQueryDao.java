/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月3日 下午1:29:45
* 
*/
package com.dlxy.server.user.dao.query;


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
* @date 创建时间：2018年7月3日 下午1:29:45
*/
public interface UserQueryDao
{
	Long countUserByParam(Map<String, Object>params) throws SQLException;
	
//	Collection<UserDTO>findUsersByPage(int start,int end ,Map<String, Object>params) throws SQLException;
	
	@Deprecated
	UserDTO findByNameOrId(String key) throws SQLException;
	
//	UserDTO findByUserId(Long userId) throws SQLException;
	
	
}
