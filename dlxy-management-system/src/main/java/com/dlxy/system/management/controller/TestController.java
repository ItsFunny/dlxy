///**
//*
//* @author joker 
//* @date 创建时间：2018年6月8日 下午3:06:33
//* 
//*/
//package com.dlxy.system.management.controller;
//
//import java.util.Collection;
//import java.util.HashMap;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.dlxy.article.server.service.IArticleService;
//import com.dlxy.common.dto.ArticleDTO;
//import com.dlxy.common.event.AppEvent;
//import com.dlxy.common.event.AppEventPublisher;
//import com.dlxy.common.event.Events;
//import com.dlxy.system.management.config.property.DlxyProperty;
//
///**
//* 
//* @author joker 
//* @date 创建时间：2018年6月8日 下午3:06:33
//*/
//@Controller
//public class TestController
//{
//	@Autowired
//	private DlxyProperty dlxyProperty;
//	@Autowired
//	private AppEventPublisher appEventPublisher;
//	@Autowired
//	private IArticleService articleService;
//	@RequestMapping("/test1")
//	@ResponseBody
//	public String test1()
//	{
//		return dlxyProperty.toString();
//	}
//	@RequestMapping("/test2")
//	public ModelAndView test2()
//	{
//		return new ModelAndView("test");
//	}
//	@RequestMapping("/test3")
//	public void testSendMessage()
//	{
//
//		HashMap<String, Object>data=new HashMap<>();
//		data.put("userId", 1);
//		data.put("detail", "del message");
//		AppEvent event=new AppEvent(data,Events.UserRecordLog.name());
//		appEventPublisher.publish(event);
//	}
////	@RequestMapping("/test3")
////	public void test3()
////	{
////		Collection<ArticleDTO> findAllArticlesExceptRecommend = articleService.findAllArticlesExceptRecommend();
////		for (ArticleDTO articleDTO : findAllArticlesExceptRecommend)
////		{
////			System.out.println(articleDTO);
////		}
////	}
//	
//
//}
