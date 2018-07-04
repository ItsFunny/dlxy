/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月4日 下午5:32:08
* 
*/
package com.dlxy.server.user.dao.query;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月4日 下午5:32:08
*/
public interface UserArticleQueryDao
{
	Long countByParam(Map<String, Object>params) throws SQLException;
	//seems to wrong place here
	Collection<Map<String, Object>>findByPage(Map<String, Object>params) throws SQLException;
}
