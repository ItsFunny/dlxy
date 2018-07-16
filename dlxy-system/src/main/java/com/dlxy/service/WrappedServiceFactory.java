/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月16日 下午6:24:35
* 
*/
package com.dlxy.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月16日 下午6:24:35
*/
public class WrappedServiceFactory
{
	private  List<Observer>observers;
	
	public WrappedServiceFactory(List<Observer> observers)
	{
		this.observers=observers;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized  <T extends Observable> T generate(Class<? extends Observable> class1)
	{
		T t=null;
		synchronized (this.observers)
		{
			try
			{
				t= (T) class1.newInstance();
				for (Observer observer : observers)
				{
					t.addObserver(observer);
				}
			} catch (InstantiationException e)
			{
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		return t;
	}
}
