/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月19日 上午7:11:54
* 
*/
package com.dlxy.common.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月19日 上午7:11:54
*/
public abstract class AbstractDlxyTreeComposite
{
	private  AbstractDlxyTreeComposite parent;
	
	private List<AbstractDlxyTreeComposite>childs;

	public AbstractDlxyTreeComposite()
	{
		super();
		this.childs=new ArrayList<>();
	}


	public AbstractDlxyTreeComposite getParent()
	{
		return parent;
	}


	public void setParent(AbstractDlxyTreeComposite parent)
	{
		this.parent = parent;
	}


	public List<AbstractDlxyTreeComposite> getChilds()
	{
		return childs;
	}


	public void setChilds(List<? extends AbstractDlxyTreeComposite> childs)
	{
		this.childs = (List<AbstractDlxyTreeComposite>) childs;
	}



	
	

}

