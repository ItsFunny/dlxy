/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 下午4:23:05
* 
*/
package com.dlxy.server.user.dao.query;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.UserRecordDTO;


/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月2日 下午4:23:05
*/
public interface UserRecordQueryDao
{
	Long countByParam(Map<String, Object>params) throws SQLException;
	//seems to wrong place here
	Collection<Map<String, Object>>findByPage(Map<String, Object>params) throws SQLException;
	
	/*
	 * find user all records
	 */
	Collection<UserRecordDTO> findRecordsByPage(int start, int end, Map<String, Object> params) throws SQLException;

}
