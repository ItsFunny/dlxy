/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月15日 下午4:27:10
* 
*/
package com.dlxy.system.batch.service;

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
	String ARTICLE_VISIT_COUNT="ARTICLE_VISIT_COUNT:%s";
	
	//batch 会自动将其更新到数据库中
	Integer ARTICLE_VISIT_COUNT_INTERVAL=60*60*60*24;
	
	void set(String key,String value,Integer mills);
	
	void set(String key,String value);
	
	
	String get(String key);
	
	void del(String key);
	boolean isAvaliable();
}
