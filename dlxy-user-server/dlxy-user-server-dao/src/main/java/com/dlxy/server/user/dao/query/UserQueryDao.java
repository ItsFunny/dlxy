/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月3日 下午1:29:45
* 
*/
package com.dlxy.server.user.dao.query;


import java.sql.SQLException;

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
	UserDTO findByNameOrId(String key) throws SQLException;
	
	UserDTO findByUserId(Long userId) throws SQLException;
}
