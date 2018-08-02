/**
*
* @author joker 
* @date 创建时间：2018年8月2日 下午2:09:05
* 
*/
package com.dlxy.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.crypto.interfaces.PBEKey;

/**
 * 
 * @author joker
 * @date 创建时间：2018年8月2日 下午2:09:05
 */
public class WrappedServiceFactory
{
	private List<Observer> ovservers;

	private Map<String, Observable> storage;

	public WrappedServiceFactory()
	{
		this.storage=new HashMap<String, Observable>();
	}
	@SuppressWarnings("unchecked")
	public <T extends Observable> T create(String name)
	{
		Observable observable = storage.get(name);
		if (null == observable)
		{
			try
			{
				observable = (Observable) Class.forName(name).newInstance();
				for (Observer observer : ovservers)
				{
					observable.addObserver(observer);
				}
				storage.put(name, observable);
			} catch (InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		return (T) observable;
	}

	@SuppressWarnings("unchecked")
	public <T extends Observable> T create(Class<T> type)
	{
		String name = type.getName();
		Observable observable = storage.get(name);
		if (null == observable)
		{
			try
			{
				observable = type.newInstance();
				for (Observer observer : ovservers)
				{
					observable.addObserver(observer);
				}
				storage.put(name, observable);
			} catch (InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		return (T) observable;
	}

	public void init(String observables)
	{
		String[] strings = observables.split(",");
		for (String string : strings)
		{
			create(string);
		}
	}

	public List<Observer> getOvservers()
	{
		return ovservers;
	}

	public void setOvservers(List<Observer> ovservers)
	{
		this.ovservers = ovservers;
	}

}
