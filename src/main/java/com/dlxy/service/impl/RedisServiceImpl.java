/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午3:51:55
* 
*/
package com.dlxy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.service.IRedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月1日 下午3:51:55
 */
public class RedisServiceImpl implements IRedisService
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
			if (null != jedis)
			{
				jedis.close();
			}
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
			if (null != jedis)
			{
				jedis.close();
			}
		}

	}

}
