/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 下午5:16:39
* 
*/
package com.dlxy.system.management.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRecordDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月2日 下午5:16:39
*/
public interface IUserMangementWrappedService
{
	PageDTO<Collection<Map<String, Object>>>findUserArticlesByPage(int pageSize,int pageNum,Map<String, Object>p) throws SQLException;
	
	PageDTO<Collection<UserRecordDTO>>findUserRecords(int pageSize,int pageNum,Map<String, Object>params) throws SQLException;
	
	PageDTO<Collection<UserDTO>> findUsersByPage(int pageSize,int pageNum,Map<String, Object>params) throws SQLException;
	
	void addUser(Long userId,UserDTO userDTO) throws SQLException;
}
