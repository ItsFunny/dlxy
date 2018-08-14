package com.dlxy.system.batch.service;

public interface IRedisService
{
	String ARTICLE_VISIT_COUNT="ARTICLE_VISIT_COUNT:%s";
	
	String USER_VISIT_HISTORY="USER_VISIT_COUNTS:%s";
	
	String BANED_IP="BANED_IP";
	
	//batch 会自动将其更新到数据库中
	Integer ARTICLE_VISIT_COUNT_INTERVAL=60*60*60*24;
	
	void set(String key,String value,Integer mills);
	
	void set(String key,String value);
	
	void expire(String key,Integer interval);
	
	String get(String key);
	
	void del(String key);
	
	boolean isAvaliable();
}
