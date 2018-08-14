/**
*
* @author joker 
* @date 创建时间：2018年8月3日 上午9:08:36
* 
*/
package com.dlxy.config;

import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class DlxyObservervable extends Observable
{
	public void setObs(List<Observer> obs)
	{
		for (Observer observer : obs)
		{
			super.addObserver(observer);
		}
	}
}
