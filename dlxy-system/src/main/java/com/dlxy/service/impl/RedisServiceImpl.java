package com.dlxy.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.service.IRedisService;
import com.sun.codemodel.internal.JWhileLoop;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

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
		// jedis.hmget(key, fields)
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

	@Override
	public Set<String> getKeysByPrefix(String prefix)
	{
		Jedis jedis = null;
		Set<String> keys = null;
		try
		{
			jedis = getJedis();
			keys = jedis.keys(prefix + "**");
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
		return keys;
	}

	@Override
	public void zAdd(String key, Double score, String memberJson)
	{
		Jedis jedis = null;
		try
		{
			jedis = getJedis();
			jedis.zadd(key, score, memberJson);
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
	public Set<String> zRevrange(String key, Long start, Long end)
	{

		Jedis jedis = null;
		Set<String> set = null;
		try
		{
			jedis = getJedis();
			set = jedis.zrevrange(key, start, end);
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
		return set;
	}

	@Override
	public void incr(String key)
	{
		Jedis jedis = null;
		try
		{
			jedis = getJedis();
			jedis.incr(key);
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
	public void zRem(String key,String ...members)
	{
		Jedis jedis = null;
		try
		{
			jedis = getJedis();
			jedis.zrem(key, (String[]) members);	
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
