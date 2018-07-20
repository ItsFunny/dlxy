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
public abstract class AbstractDlxyTreeComponent
{
	private  AbstractDlxyTreeComponent parent;
	
	private List<AbstractDlxyTreeComponent>childs;

	
	public AbstractDlxyTreeComponent()
	{
		super();
		this.childs=Collections.emptyList();
	}


	public AbstractDlxyTreeComponent getParent()
	{
		return parent;
	}


	public void setParent(AbstractDlxyTreeComponent parent)
	{
		this.parent = parent;
	}


	public List<AbstractDlxyTreeComponent> getChilds()
	{
		return childs;
	}


	public void setChilds(List<? extends AbstractDlxyTreeComponent> childs)
	{
		this.childs = (List<AbstractDlxyTreeComponent>) childs;
	}



	
	

}

