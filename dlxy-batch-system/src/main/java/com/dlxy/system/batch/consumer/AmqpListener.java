/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午4:13:05
* 
*/
package com.dlxy.system.batch.consumer;

import com.dlxy.common.event.AppEvent;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月29日 下午4:13:05
*/
public interface AmqpListener
{
	String getQueueName();
	
	void process(AppEvent event);
	
	
}
