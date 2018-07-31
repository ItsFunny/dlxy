/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 上午11:14:58
* 
*/
package com.dlxy.service.command;

import java.sql.SQLException;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.HTMLReader.ParagraphAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.server.article.service.IArticleService;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月2日 上午11:14:58
 */
public class ArticleGroup implements IGroup
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
		Long[] articleIds=(Long[]) params.get("articleIds");
		articleServiceImpl.deleteArticlesInBatch(articleIds);
	}

}
