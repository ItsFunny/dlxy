package com.dlxy.service;

import java.util.Map;
import java.util.Set;

public interface IRedisService
{
	String ONLINE_USER_PREFIX="ONLINE_USER_PREFIX";
	
	String WEB_VISIT_TOTAL_COUT="WEB_VISIT_TOTAL_COUT";
	
	String PER_DAY_VISIT_COUNT="PER_DAY_VISIT_COUNT:%s";
	
	String ARTICLE_VISIT_COUNT="ARTICLE_VISIT_COUNT:%s";
	
	String USER_VISIT_HISTORY="USER_VISIT_COUNTS:%s";
	
	String BANED_IP="BANED_IP";
	
	String ARTICLE_VISIT_RANGE="ARTICLE_VISIT_RANGE";
	
	Set<String> getKeysByPrefix(String prefix);
	
	Integer ARTICLE_VISIT_COUNT_INTERVAL=60*60*60*24;
	
	void set(String key,String value,Integer mills);
	
	void set(String key,String value);
	
	void zAdd(String key,Double score,String memberJson);
	
	Set<String>zRevrange(String key,Long start,Long end);
	
	void zRem(String key,String ...members);
	
	void incr(String key);
	
	void expire(String key,Integer interval);
	
	String get(String key);
	
	Boolean del(String key);
	
	boolean isAvaliable();
}
