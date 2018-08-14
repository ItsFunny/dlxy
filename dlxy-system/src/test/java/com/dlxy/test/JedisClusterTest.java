///**
//*
//* @author joker 
//* @date 创建时间：2018年8月11日 下午10:43:01
//* 
//*/
//package com.dlxy.test;
//
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;
//
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.junit.Test;
//
//import redis.clients.jedis.HostAndPort;
//import redis.clients.jedis.JedisCluster;
//
///**
//* 
//* @author joker 
//* @date 创建时间：2018年8月11日 下午10:43:01
//*/
//public class JedisClusterTest
//{
//	@Test
//	public void testJedis()
//	{
//		Set<HostAndPort>nodes=new HashSet<>();
//		nodes.add(new HostAndPort("120.78.240.211", 7000));
//		nodes.add(new HostAndPort("120.78.240.211", 7001));
//		nodes.add(new HostAndPort("120.78.240.211", 7002));
//		nodes.add(new HostAndPort("120.78.240.211", 7003));
//		nodes.add(new HostAndPort("120.78.240.211", 7004));
//		GenericObjectPoolConfig poolConfig=new GenericObjectPoolConfig();
//		poolConfig.setMaxIdle(5);
//		poolConfig.setMaxTotal(200);
//		JedisCluster jedisCluster=new JedisCluster(nodes, 10000, 10000, 3, "123456", poolConfig);
//		jedisCluster.set("test", "test1");
//		String string = jedisCluster.get("test");
//		System.out.println(string);
//	}
//	public JedisClusterTest()
//	{
//		super();
//	}
//	public JedisClusterTest(String name)
//	{
//		
//	}
//}
