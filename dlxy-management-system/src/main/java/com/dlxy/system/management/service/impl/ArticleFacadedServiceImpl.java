/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午11:32:15
* 
*/
package com.dlxy.system.management.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.server.article.service.IArticleCountService;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.user.service.IUserRecordService;
import com.dlxy.system.management.service.IArticleFacadedService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 下午11:32:15
*/
@Service
public class ArticleFacadedServiceImpl implements IArticleFacadedService
{
	@Autowired
	private IArticleCountService articleCountService;
	@Autowired
	private IArticleService articleService;
	@Autowired
	private IUserRecordService userRecordService;
	
	public PageDTO<Collection<ArticleDTO>> findArticles(int pageSize, int pageNum, Map<String, String> params) throws SQLException
	{
		Long totalCount = articleCountService.countArticlesByDetailParam(params);
		if(totalCount<1)
		{
			Collection<ArticleDTO>collection=Collections.emptyList();
			return new PageDTO<Collection<ArticleDTO>>(0L, collection);
		}
		Collection<ArticleDTO> articles = articleService.findAllArticlesExceptRecommend((pageNum-1)*pageSize, pageSize);
		return new PageDTO<Collection<ArticleDTO>>(totalCount, articles);
	}

	@Transactional
	@Override
	public void delArticle(Long userId,Long articleId)
	{
		articleService.updateArticleStatus(articleId, ArticleStatusEnum.DELETE.ordinal());
		UserRecordDTO userRecordDTO=new UserRecordDTO();
		userRecordDTO.setUserId(userId);
		userRecordDTO.setRecordDetail("delete:article:"+articleId);
		userRecordService.addRecord(userRecordDTO);
	}

}