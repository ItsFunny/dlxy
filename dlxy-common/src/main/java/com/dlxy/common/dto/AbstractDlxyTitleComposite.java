/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月19日 上午7:15:31
* 
*/
package com.dlxy.common.dto;

import java.util.ArrayList;
import java.util.Collection;
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
	private List<? super AbstractDlxyArticleComposite> articles;

	
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
	public Collection<AbstractDlxyArticleComposite> getArticles()
	{
		return (Collection<AbstractDlxyArticleComposite>) articles;
	}
	public void setArticles(List<? extends AbstractDlxyArticleComposite> articles)
	{
		this.articles = (List<AbstractDlxyArticleComposite>) articles;
	}
	
	

	
	
}
