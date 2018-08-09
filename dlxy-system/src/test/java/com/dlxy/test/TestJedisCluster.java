/**
*
* @author joker 
* @date 创建时间：2018年8月7日 下午11:19:14
* 
*/
package com.dlxy.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author joker
 * @date 创建时间：2018年8月7日 下午11:19:14
 */
public class TestJedisCluster
{
	private static JedisCluster jedisCluster = null;

	public static Set<HostAndPort> getHostAndPort(String hostAndPort)
	{
		Set<HostAndPort> hap = new HashSet<HostAndPort>();
		String[] hosts = hostAndPort.split(",");
		String[] hs = null;
		for (String host : hosts)
		{
			hs = host.split(":");
			hap.add(new HostAndPort(hs[0], Integer.parseInt(hs[1])));
		}
		return hap;
	}

	public static void test()
	{
		Set<HostAndPort> hostAndPorts = new HashSet<>();

		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(5);
		jedisPoolConfig.setMaxTotal(1024);
		jedisPoolConfig.setMaxWaitMillis(10000);
		jedisPoolConfig.setTestOnBorrow(true);
		
		
		GenericObjectPoolConfig gopc = new GenericObjectPoolConfig();
		gopc.setMaxTotal(400);
		gopc.setMaxIdle(200);
		gopc.setMaxWaitMillis(6000);
		hostAndPorts = getHostAndPort("120.78.240.211:6379,120.78.240.211:7000,120.78.240.211:7001,120.78.240.211:7002,120.78.240.211:7003,120.78.240.211:7004");
		jedisCluster = new JedisCluster(hostAndPorts, 20000, 20000, 5, "123456",jedisPoolConfig);
		jedisCluster.set("test", "ttt");
		System.out.println(jedisCluster.get("test"));
	}

	public static void main(String[] args) throws IOException, TimeoutException
	{
//		GenericObjectPoolConfig gopc = new GenericObjectPoolConfig();
//		gopc.setMaxTotal(32);
//		gopc.setMaxIdle(4);
//		gopc.setMaxWaitMillis(6000);
//		JedisPool jedisPool = new JedisPool(gopc, "120.78.240.211", 6379,
//				10000, "123456");
//		Jedis jedis = jedisPool.getResource();
//		jedis.set("joker", "test");
//		System.out.println(jedis.get("joker"));
//		jedis.close();
		
		
		
//		test();
		
		ConnectionFactory connectionFactory=new ConnectionFactory();
		connectionFactory.setHost("120.78.240.211");
		connectionFactory.setUsername("joker");
		connectionFactory.setPassword("123456");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare("test	", false	, false, false, null);
		String message="hello world";
		channel.basicPublish("", "test", null, message.getBytes());
		System.out.println("send message");
		channel.close();
		connection.close();
		
	}
}
