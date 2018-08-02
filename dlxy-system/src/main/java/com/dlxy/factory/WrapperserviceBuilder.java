/**
*
* @author joker 
* @date 创建时间：2018年8月2日 下午3:41:00
* 
*/
package com.dlxy.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;

/**
* 
* @author joker 
* @date 创建时间：2018年8月2日 下午3:41:00
*/
public class WrapperserviceBuilder implements IWrappedServieBuiler
{
	private Map<String, IWrappedService>storage=new HashMap<String, IWrappedService>();
	
	private List<Observer>observers;
	
	public WrapperserviceBuilder(List<Observer> observers)
	{
		this.observers=observers;
	}
	
	public void init(String names)
	{
		String[] ns = names.split(",");
		for (String string : ns)
		{
			create(string);
		}
	}
	
	@Override
	public IWrappedService create(String name)
	{
		IWrappedService service = storage.get(name);
		if(null==service)
		{
			try
			{
				Object obj = Class.forName(name).newInstance();
				if(!(obj instanceof IWrappedService))
				{
					throw new RuntimeException("when u want to create obj by this ,the instance must be the subClass of IWrappedService");
				}
				service=(IWrappedService) obj;
				service.setObservers(this.observers);
			} catch (InstantiationException e)
			{
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				e.printStackTrace();
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			} 
		}
		return service;
	}

}
