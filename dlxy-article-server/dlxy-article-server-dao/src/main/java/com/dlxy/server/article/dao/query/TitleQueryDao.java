/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午4:14:29
* 
*/
package com.dlxy.server.article.dao.query;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import com.dlxy.common.dto.DlxyTitleDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月1日 下午4:14:29
*/
public interface TitleQueryDao
{
	Collection<DlxyTitleDTO>findByPage(Map<String, Object>params) throws SQLException;
	
//	Long countTilte(Map<String, Object>params) throws SQLException;
}
