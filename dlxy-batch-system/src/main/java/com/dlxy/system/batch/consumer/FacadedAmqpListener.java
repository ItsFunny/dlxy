/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午5:49:55
* 
*/
package com.dlxy.system.batch.consumer;

import java.util.List;

import com.dlxy.common.event.AmqpListener;
import com.dlxy.common.event.AppEvent;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月29日 下午5:49:55
 */
public class FacadedAmqpListener implements AmqpListener
{
	private List<? extends AmqpListener> listeners;

	public FacadedAmqpListener(List<? extends AmqpListener> listeners)
	{
		this.listeners = listeners;
	}

	@Override
	public String getQueueName()
	{
		return "*";
	}

	@Override
	public void process(AppEvent event)
	{
		if(null==listeners || listeners.size()==0)
		{
			return;
		}
		for (AmqpListener amqpListener : listeners)
		{
			if (amqpListener instanceof FacadedAmqpListener)
			{
				continue;
			}
			if (amqpListener.getQueueName().equals(event.getEventType()))
			{
				amqpListener.process(event);
			}
		}
	}

}
