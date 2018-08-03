/**
*
* @author joker 
* @date 创建时间：2018年8月2日 下午3:34:57
* 
*/
package com.dlxy.defaultp;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
* 
* @author joker 
* @date 创建时间：2018年8月2日 下午3:34:57
*/
public abstract class AbstractWrappedService extends Observable implements IWrappedService
{
	@Override
	public void setObservers(List<Observer> observers)
	{
		for (Observer observer : observers)
		{
			super.addObserver(observer);
		}
	}
	
}
