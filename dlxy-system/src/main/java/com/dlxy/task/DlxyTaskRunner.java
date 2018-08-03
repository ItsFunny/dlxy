/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月27日 下午12:08:50
* 
*/
package com.dlxy.task;

import java.io.Serializable;

public abstract class DlxyTaskRunner implements Runnable
{
	public Serializable data;

	public DlxyTaskRunner(Serializable data)
	{
		this.data=data;
	}
	public Serializable getData()
	{
		return data;
	}

	public void setData(Serializable data)
	{
		this.data = data;
	}
	
}
