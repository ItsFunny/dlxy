///**
//*
//* @Description
//* @author joker 
//* @date 创建时间：2018年7月22日 上午11:12:25
//* 
//*/
//package com.dlxy.aop;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.Set;
//
//import javax.servlet.ServletRequestAttributeEvent;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.dlxy.common.dto.ArticleDTO;
//import com.dlxy.common.dto.SuspicionDTO;
//import com.dlxy.common.enums.IllegalLevelEnum;
//import com.dlxy.common.utils.JsonUtil;
//import com.dlxy.exception.DlxySuspicionException;
//import com.dlxy.model.ArticleVisitInfo;
//import com.dlxy.server.article.service.IArticleService;
//import com.dlxy.service.IRedisService;
//import com.joker.library.utils.CommonUtils;
//
//import redis.clients.jedis.exceptions.JedisException;
//
///**
// * 
// * @When
// * @Description
// * @Detail
// * @author joker
// * @date 创建时间：2018年7月22日 上午11:12:25
// */
//@Aspect
//@Component
//@Order(2)
//public class UserAccessRecord
//{
//	private Logger logger=LoggerFactory.getLogger(UserAccessRecord.class);
//	@Autowired
//	private IRedisService redisService;
//	
//	@Autowired
//	private IArticleService articleService;
//
//	@Pointcut("execution (* com.dlxy.controller.PortalController.*(..))")
//	public void recordArticle()
//	{
//	}
//
//	@Before("recordArticle()")
//	public void before(JoinPoint joinPoint)
//	{
//		Object[] args = joinPoint.getArgs();
//		String articleIdStr = (String) args[0];
////		HttpServletResponse response = (HttpServletResponse) args[2];
//		String key=String.format(IRedisService.ARTICLE_VISIT_COUNT, articleIdStr);
//		String json=null;
//		ArticleVisitInfo visitInfo=null;
//		try
//		{
//			
//			synchronized (this)
//			{
//				 json = redisService.get(key);
//				if (StringUtils.isEmpty(json))
//				{
//					ArticleDTO articleDTO = articleService.findByArticleId(Long.parseLong(articleIdStr));
//					if(articleDTO!=null)
//					{
//						 visitInfo=new ArticleVisitInfo();
//						visitInfo.setVisitCount(articleDTO.getVisitCount());
//						json = JsonUtil.obj2Json(visitInfo);
//						redisService.set(String.format(IRedisService.ARTICLE_VISIT_COUNT, articleIdStr), json);
//					}else {
//						visitInfo=JsonUtil.json2Object(json, ArticleVisitInfo.class);
//					}
//				}
//			}
//		} catch (JedisException e)
//		{
//			logger.error("[查询文章记录aop] error redis服务器挂了");
//			//发送邮件
//		}
//		Set<Map<String, Long>> visitors = visitInfo.getVisitors();
//		
//		System.out.println("11111");
//	}
//}
