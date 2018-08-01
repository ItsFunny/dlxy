/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午3:51:55
* 
*/
package com.dlxy.system.batch.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.system.batch.service.IRedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

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

	public synchronized Jedis getJedis()
	{
		Jedis jedis = null;
		if (null != jedisPool)
		{
			try
			{
				jedis = jedisPool.getResource();
			} catch (JedisException e)
			{
				if (null != jedis)
				{
					jedis.close();
				}
				throw e;
			}
		}
		return jedis;
	}

	@Override
	public String get(String key)
	{
		Jedis jedis = getJedis();
		try
		{
			String json = jedis.get(key);
			return json;
		} catch (JedisException e)
		{
			if (null != jedis)
			{
				jedis.close();
			}
			throw e;
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
		Jedis jedis = getJedis();
		try
		{
			jedis.del(key);
		} catch (JedisException e)
		{
			if (null != jedis)
			{
				jedis.close();
			}
			throw e;
		} finally
		{
			if (null != jedis)
			{
				jedis.close();
			}
		}

	}

	@Override
	public void set(String key, String value, Integer mills)
	{
		Jedis jedis = getJedis();
		try
		{
			jedis.set(key, value);
			jedis.expire(key, mills);
		} catch (JedisException e)
		{
			if (null != jedis)
			{
				jedis.close();
			}
			throw e;
		} finally
		{
			if (null != jedis)
			{
				jedis.close();
			}
		}
	}

	@Override
	public void set(String key, String value)
	{
		Jedis jedis = getJedis();
		try
		{
			jedis.set(key, value);
		} catch (Exception e)
		{
			if (null != jedis)
			{
				jedis.close();
			}
			throw e;
		} finally
		{
			if (null != jedis)
			{
				jedis.close();
			}
		}
	}

	@Override
	public boolean isAvaliable()
	{
		if (null != jedisPool)
		{
			try
			{
				jedisPool.getResource().close();
				return true;
			} catch (Exception e)
			{
				return false;
			}
		}
		return false;
	}

	@Override
	public void expire(String key, Integer interval)
	{
		Jedis jedis = null;
		try
		{
			jedis = getJedis();
			jedis.expire(key, interval);
		} catch (Exception e)
		{
			if (null != jedis)
			{
				jedis.close();
			}
			throw e;
		} finally
		{
			if (null != jedis)
			{
				jedis.close();
			}
		}
	}

}
