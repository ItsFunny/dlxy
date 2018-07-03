/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午7:01:14
* 
*/
package com.dlxy.server.user.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import com.dlxy.common.dto.UserRecordDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月29日 下午7:01:14
*/
public interface IUserRecordService
{
	void addRecord(UserRecordDTO userRecordDTO);
	
	Long countByParam(Map<String, Object>params) throws SQLException;
	
	/*
	 * 查询用户发表了什么文章
	 */
	Collection<Map<String, Object>>findByPage(int pageSize,int pageNum,Map<String, Object>params) throws SQLException;
	
	
	/*
	 * 查询用户的操作记录
	 */
	Collection<Map<String, Object>>findRecordByPage(int pageSize,int pageNum,Map<String, Object>params);
	
}
