/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午11:26:29
* 
*/
package com.dlxy.article.server.service;

import java.sql.SQLException;
import java.util.Map;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 下午11:26:29
*/
public interface IArticleCountService
{
	Long countArticlesByDetailParam(Map<String, String>params) throws SQLException;
}	
