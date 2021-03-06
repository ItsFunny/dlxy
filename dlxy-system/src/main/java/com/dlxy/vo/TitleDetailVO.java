package com.dlxy.vo;

import java.util.Collection;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.PageDTO;

public class TitleDetailVO
{
	private PageDTO<Collection<ArticleDTO>> articlePage;
	
	private DlxyTitleDTO parentAndChilds;
	
	private DlxyTitleDTO titleSelf;
	
	public DlxyTitleDTO getParentAndChilds()
	{
		return parentAndChilds;
	}

	public void setParentAndChilds(DlxyTitleDTO parentAndChilds)
	{
		this.parentAndChilds = parentAndChilds;
	}

	public DlxyTitleDTO getTitleSelf()
	{
		return titleSelf;
	}

	public void setTitleSelf(DlxyTitleDTO titleSelf)
	{
		this.titleSelf = titleSelf;
	}

	public PageDTO<Collection<ArticleDTO>> getArticlePage()
	{
		return articlePage;
	}

	public void setArticlePage(PageDTO<Collection<ArticleDTO>> articlePage)
	{
		this.articlePage = articlePage;
	}
	
}
 