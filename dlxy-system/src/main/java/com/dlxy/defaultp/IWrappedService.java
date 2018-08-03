package com.dlxy.defaultp;

import java.util.List;
import java.util.Observer;

public interface IWrappedService
{
	void setObservers(List<Observer> observers);
}
