/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 上午11:06:26
* 
*/
package com.dlxy.service.command;

import java.sql.SQLException;
import java.util.Map;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月2日 上午11:06:26
*/
public interface IGroup
{
	void add(Map<String, Object>map) throws SQLException;
	
	void update(Map<String, Object>params) throws SQLException;
}
