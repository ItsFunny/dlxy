/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月19日 上午7:15:31
* 
*/
package com.dlxy.common.dto;

import java.util.ArrayList;
import java.util.List;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月19日 上午7:15:31
*/
public abstract class AbstractDlxyTitleComposite extends AbstractDlxyTreeComponent
{
	private List<AbstractDlxyArticleComposite> articles;

	
	public AbstractDlxyTitleComposite()
	{
		super();
		this.articles=new ArrayList<AbstractDlxyArticleComposite>();
	}
	public void addArticle(AbstractDlxyArticleComposite abstractDlxyArticleComposite)
	{
		abstractDlxyArticleComposite.setBelongTo(this);
		this.articles.add(abstractDlxyArticleComposite);
	}
	public void addChild(AbstractDlxyTitleComposite abstractDlxyTitleComposite)
	{
		abstractDlxyTitleComposite.setParent(this);
		super.getChilds().add(abstractDlxyTitleComposite);
	}
	
	
	public List<AbstractDlxyArticleComposite> getArticles()
	{
		return articles;
	}
	public void setArticles(List<AbstractDlxyArticleComposite> articles)
	{
		this.articles = articles;
	}

	
	
}
