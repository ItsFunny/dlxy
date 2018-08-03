package com.dlxy.service.command;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.service.IArticleWrappedService;

public class ArticleReceiver implements IReceiver
{

	@Autowired
	private IArticleService articleServiceImpl;

	@Override
	public void add(Map<String, Object> map)
	{
		ArticleDTO articleDTO = (ArticleDTO) map.get("articleDTO");
		articleServiceImpl.insertOrUpdate(articleDTO);
	}

	@Override
	public void update(Map<String, Object> params) throws SQLException
	{
		ArticleDTO articleDTO=(ArticleDTO) params.get("articleDTO");
		articleServiceImpl.update(articleDTO);
	}

	@Override
	public void delete(Map<String, Object> params)
	{
		@SuppressWarnings("unchecked")
		List<Long>articleIds=(List<Long>) params.get("articleIdList");
		articleServiceImpl.deleteArticlesInBatch(articleIds);
	}

}
