/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午7:25:50
* 
*/
package com.dlxy.common.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月28日 下午7:25:50
 */
public class AppEventRabbitMQPublisher implements AppEventPublisher
{

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void publish(AppEvent event)
	{
		event.setStartTimeStamp(System.currentTimeMillis());
		rabbitTemplate.convertAndSend(event.getEventType().toUpperCase(), event);
	}

}
