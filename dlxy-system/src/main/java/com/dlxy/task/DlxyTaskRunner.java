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
