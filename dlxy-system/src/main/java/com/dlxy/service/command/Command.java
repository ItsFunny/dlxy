/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 上午11:21:56
* 
*/
package com.dlxy.service.command;

import java.util.Map;
import java.util.Observable;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月2日 上午11:21:56
 */
public abstract class Command extends Observable
{
	protected ArticleGroup articleGroup;
	protected PictureGroup pictureGroup;
	protected UserArticleGroup userArticleGroup;

	public abstract void execute(Map<String, Object> param);

	protected ArticleGroup getArticleGroup()
	{
		return articleGroup;
	}

	protected void setArticleGroup(ArticleGroup articleGroup)
	{
		this.articleGroup = articleGroup;
	}

	protected PictureGroup getPictureGroup()
	{
		return pictureGroup;
	}

	protected void setPictureGroup(PictureGroup pictureGroup)
	{
		this.pictureGroup = pictureGroup;
	}

	protected UserArticleGroup getUserArticleGroup()
	{
		return userArticleGroup;
	}

	protected void setUserArticleGroup(UserArticleGroup userArticleGroup)
	{
		this.userArticleGroup = userArticleGroup;
	}

}
