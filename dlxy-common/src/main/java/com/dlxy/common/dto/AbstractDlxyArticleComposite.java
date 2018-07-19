/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月19日 上午7:17:12
* 
*/
package com.dlxy.common.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月19日 上午7:17:12
*/
public abstract class AbstractDlxyArticleComposite extends AbstractDlxyTreeComponent
{
	
	private AbstractDlxyArticleComposite previous;
	
	private AbstractDlxyArticleComposite next;
	
	private AbstractDlxyTitleComposite belongTo;

	public AbstractDlxyTitleComposite getBelongTo()
	{
		return belongTo;
	}

	public void setBelongTo(AbstractDlxyTitleComposite belongTo)
	{
		this.belongTo = belongTo;
	}

	public AbstractDlxyArticleComposite getPrevious()
	{
		return previous;
	}

	public void setPrevious(AbstractDlxyArticleComposite previous)
	{
		this.previous = previous;
	}

	public AbstractDlxyArticleComposite getNext()
	{
		return next;
	}

	public void setNext(AbstractDlxyArticleComposite next)
	{
		this.next = next;
	}
	
		
	
	
}
