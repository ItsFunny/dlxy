///**
//*
//* @Description
//* @author joker 
//* @date 创建时间：2018年6月28日 下午11:32:15
//* 
//*/
//package com.dlxy.system.management.service.impl;
//
//import java.sql.SQLException;
//import java.util.Collection;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.dlxy.common.annotation.UserRecordAnnotation;
//import com.dlxy.common.dto.ArticleDTO;
//import com.dlxy.common.dto.PageDTO;
//import com.dlxy.common.dto.UserRecordDTO;
//import com.dlxy.common.utils.PageResultUtil;
//import com.dlxy.server.article.service.IArticleCountService;
//import com.dlxy.server.article.service.IArticleService;
//import com.dlxy.server.user.service.IUserRecordService;
//import com.dlxy.system.management.service.IArticleManagementWrappedService;
//
///**
// * 
// * @When
// * @Description
// * @Detail
// * @author joker
// * @date 创建时间：2018年6月28日 下午11:32:15
// */
//@Service
//public class ArticleFacadedServiceImpl implements IArticleManagementWrappedService
//{
//	@Autowired
//	private IArticleCountService articleCountService;
//	@Autowired
//	private IArticleService articleService;
//	@Autowired
//	private IUserRecordService userRecordService;
//
////	public PageDTO<Collection<ArticleDTO>> findArticles(int pageSize, int pageNum, Map<String, Object> params)
////			throws SQLException
////	{
////		Long totalCount = articleCountService.countArticlesByDetailParam(params);
////		if (totalCount < 1)
////		{
////			Collection<ArticleDTO> collection = Collections.emptyList();
////			return new PageDTO<Collection<ArticleDTO>>(0L, collection);
////		}
////		Collection<ArticleDTO> articles = articleService.findAllArticlesExceptRecommend((pageNum - 1) * pageSize,
////				pageSize);
////		return new PageDTO<Collection<ArticleDTO>>(totalCount, articles);
////	}
//	
////	@Transactional
////	@Override
////	public void delArticle(Long userId, Long articleId)
////	{
////		articleService.updateArticleStatus(articleId, ArticleStatusEnum.DELETE.ordinal());
////		UserRecordDTO userRecordDTO = new UserRecordDTO();
////		userRecordDTO.setUserId(userId);
////		userRecordDTO.setRecordDetail("delete:article:" + articleId);
////		userRecordService.addRecord(userRecordDTO);
////	}
//
////	@UserRecordAnnotation(dealWay="update:article")
////	@Transactional
////	@Override
////	public void updateArticlesInBatch(Long userId, Long[] articleIds, int status)
////	{
////		articleService.updateArticleStatusInBatch(articleIds, status);
////		UserRecordDTO userRecordDTO = new UserRecordDTO();
////		userRecordDTO.setUserId(userId);
////		StringBuilder sb = new StringBuilder();
////		for (Long long1 : articleIds)
////		{
////			sb.append(String.valueOf(long1) + ",");
////		}
////		userRecordDTO.setRecordDetail("update:article:" + sb.toString());
////		userRecordService.addRecord(userRecordDTO);
////	}
//	
//	
//	@Override
//	public PageDTO<Collection<ArticleDTO>> findByParams(int start, int end, Map<String, Object> params)
//			throws SQLException
//	{
//		Long totalCount = articleCountService.countArticlesByDetailParam(params);
//		if (totalCount <= 0)
//		{
//			return PageResultUtil.emptyPage();
//		}
//		Collection<ArticleDTO> datas = articleService.findByParam(params, start, end);
//		return new PageDTO<Collection<ArticleDTO>>(totalCount, datas);
//	}
//
//	@Override
//	public void updateArticlesInBatch(Long userId, Long[] articleIds, int status)
//	{
//		// TODO Auto-generated method stub
//		
//	}
//
//}
