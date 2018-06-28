/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午7:02:17
* 
*/
package com.dlxy.system.management.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dlxy.system.management.config.property.DlxyProperty;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 下午7:02:17
*/
@Configuration
public class ManagementRabbitMQConfiguration
{
	@Autowired
	private DlxyProperty dlxyProperty;
	
	@Bean
	public ConnectionFactory connectionFactory() throws Exception
	{
		RabbitConnectionFactoryBean rabbitConnectionFactoryBean=new RabbitConnectionFactoryBean();
		rabbitConnectionFactoryBean.setHost(dlxyProperty.getAmqpHost());
		rabbitConnectionFactoryBean.setUsername(dlxyProperty.getUsername());
		rabbitConnectionFactoryBean.setPassword(dlxyProperty.getPassword());
		rabbitConnectionFactoryBean.afterPropertiesSet();
		CachingConnectionFactory cachingConnectionFactory=new CachingConnectionFactory(rabbitConnectionFactoryBean.getObject());
		return cachingConnectionFactory;
	}
	@Bean
	public RabbitTemplate rabbitTemplate() throws Exception
	{
		RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory());
		rabbitTemplate.setExchange("dlxy");
//		rabbitTemplate.setMessageConverter(messageConverter);
		return rabbitTemplate;
	}
	@Bean
	public RabbitAdmin rabbitAdmin() throws Exception
	{
		RabbitAdmin rabbitAdmin=new RabbitAdmin(connectionFactory());
		return rabbitAdmin;
	}
	
}
