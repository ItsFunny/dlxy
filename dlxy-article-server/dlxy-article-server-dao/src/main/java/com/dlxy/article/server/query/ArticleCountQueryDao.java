/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午11:13:39
* 
*/
package com.dlxy.article.server.query;

import java.sql.SQLException;
import java.util.Map;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 下午11:13:39
*/
public interface ArticleCountQueryDao
{
	Long coutArticles(Map<String, String>params) throws SQLException;
}
