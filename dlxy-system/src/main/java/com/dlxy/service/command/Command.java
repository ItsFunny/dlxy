package com.dlxy.service.command;

import java.util.Map;

import com.dlxy.config.DlxyObservervable;

public abstract class Command extends DlxyObservervable
{
	protected ArticleReceiver articleGroup;
	protected PictureReceiver pictureGroup;
	protected UserArticleReceiver userArticleGroup;

	public abstract void execute(Map<String, Object> param);

	protected ArticleReceiver getArticleGroup()
	{
		return articleGroup;
	}

	protected void setArticleGroup(ArticleReceiver articleGroup)
	{
		this.articleGroup = articleGroup;
	}

	protected PictureReceiver getPictureGroup()
	{
		return pictureGroup;
	}

	protected void setPictureGroup(PictureReceiver pictureGroup)
	{
		this.pictureGroup = pictureGroup;
	}

	protected UserArticleReceiver getUserArticleGroup()
	{
		return userArticleGroup;
	}

	protected void setUserArticleGroup(UserArticleReceiver userArticleGroup)
	{
		this.userArticleGroup = userArticleGroup;
	}

}
