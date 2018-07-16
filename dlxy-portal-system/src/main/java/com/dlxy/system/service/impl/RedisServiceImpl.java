/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月10日 上午10:10:38
* 
*/
package com.dlxy.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.system.service.RedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月10日 上午10:10:38
 */
public class RedisServiceImpl implements RedisService
{
	@Autowired
	private JedisPool jedisPool;

	@Override
	public String get(String key)
	{
		Jedis jedis = null;
		try
		{
			jedis = jedisPool.getResource();
			String json = jedis.get(key);
			return json;
		} finally
		{
			jedis.close();
		}

	}

	@Override
	public void del(String key)
	{
		Jedis jedis = null;
		try
		{
			jedis = jedisPool.getResource();
			jedis.del(key);
		} finally
		{
			jedis.close();
		}
	}

}
