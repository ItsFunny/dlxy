package com.dlxy.server.user.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

/**
* 
* @When
* @Description 用户发表了哪些文章
* @Detail
* @author joker 
* @date 创建时间：2018年7月4日 下午5:35:38
*/
public interface IUserArticleService
{
	Long countByParam(Map<String, Object>params) throws SQLException;
	
	/*
	 * 查询用户发表了什么文章
	 */
	Collection<Map<String, Object>>findByPage(int pageSize,int pageNum,Map<String, Object>params) throws SQLException;
}
