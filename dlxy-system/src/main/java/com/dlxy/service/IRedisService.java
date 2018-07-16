/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月15日 下午4:27:10
* 
*/
package com.dlxy.service;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月15日 下午4:27:10
*/
public interface IRedisService
{
	String get(String key);
	
	void del(String key);
}
