///**
//*
//* @author joker 
//* @date 创建时间：2018年8月12日 上午11:05:00
//* 
//*/
//package com.dlxy.test;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.dlxy.config.DlxySystemConfiiguration;
//import com.dlxy.config.DlxySystemSpringConfiguration;
//import com.dlxy.service.IRedisService;
//
///**
//* 
//* @author joker 
//* @date 创建时间：2018年8月12日 上午11:05:00
//*/
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes=DlxySystemConfiiguration.class)
//public class InitDb
//{
//	@Autowired
//	private IRedisService redisService;
//	
//	@Test
//	public void test()
//	{
////		ApplicationContext context=new AnnotationConfigApplicationContext(DlxySystemConfiiguration.class,DlxySystemSpringConfiguration.class);
////		IRedisService redisService = context.getBean(IRedisService.class);
//		System.out.println(redisService);
//	}
//}
