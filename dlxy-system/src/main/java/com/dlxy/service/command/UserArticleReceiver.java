package com.dlxy.service.command;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.server.article.service.IUserArticleService;

public class UserArticleReceiver implements IReceiver
{
	@Autowired
	private IUserArticleService userArticleService;
	@Override
	public void add(Map<String, Object> map)
	{
		ArticleDTO articleDTO = (ArticleDTO) map.get("articleDTO");
		userArticleService.addUserArticle(articleDTO.getUserId(), articleDTO.getArticleId(), articleDTO.getRealname());
	}

	@Override
	public void update(Map<String, Object> params) throws SQLException
	{
		
	}


	@Override
	public void delete(Map<String, Object> params)
	{
		// TODO Auto-generated method stub
		
	}

}
