/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午3:51:22
* 
*/
package com.dlxy.system.service;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月1日 下午3:51:22
*/
public interface RedisService
{
	String get(String key);
	
	void del(String key);
}
