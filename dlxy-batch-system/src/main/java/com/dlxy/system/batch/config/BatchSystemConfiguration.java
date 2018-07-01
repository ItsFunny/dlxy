/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午2:59:37
* 
*/
package com.dlxy.system.batch.config;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.dlxy.common.event.Events;
import com.dlxy.system.batch.consumer.AmqpListener;
import com.dlxy.system.batch.consumer.FacadedAmqpListener;
import com.dlxy.system.batch.consumer.detail.UserIllegalLogListener;
import com.dlxy.system.batch.consumer.detail.UserRecordListener;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月29日 下午2:59:37
 */
@Configuration
@EnableConfigurationProperties(value =
{ DlxyProperty.class })
@ComponentScan(basePackages= {
		"com.dlxy.server.user.service"
})
@MapperScan(basePackages= {
		"com.dlxy.server.user.dao"
})
public class BatchSystemConfiguration
{
	@Autowired
	private DlxyProperty dlxyProperty;
	
	@Bean
	public DataSource dataSource()
	{
		DruidDataSource dataSource=new DruidDataSource();
		dataSource.setUsername(dlxyProperty.getDbUsername());
		dataSource.setPassword(dlxyProperty.getDbPassword());
		dataSource.setUrl(dlxyProperty.getDbUrl());
		dataSource.setDriverClassName(dlxyProperty.getDbDriverClassName());
		return dataSource;
	}
	@Bean
	public QueryRunner queryRunner()
	{
		return new QueryRunner(dataSource());
	}
	@Bean
	public SqlSessionFactoryBean sqlSessionFactory()
	{
		SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		org.apache.ibatis.session.Configuration configuration=new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		sqlSessionFactoryBean.setConfiguration(configuration);
//		sqlSessionFactoryBean.setMapperLocations(mapperLocations);
		return sqlSessionFactoryBean;
	}
	@ConditionalOnProperty(prefix="dlxy.config",name="amqp-enabled",matchIfMissing=false)
	@Bean
	public ConnectionFactory connectionFactory() throws Exception
	{
		RabbitConnectionFactoryBean rabbitConnectionFactoryBean=new RabbitConnectionFactoryBean();
		rabbitConnectionFactoryBean.setHost(dlxyProperty.getAmqpHost());
		rabbitConnectionFactoryBean.setUsername(dlxyProperty.getAmqpUsername());
		rabbitConnectionFactoryBean.setPassword(dlxyProperty.getAmqpPassword());
		rabbitConnectionFactoryBean.afterPropertiesSet();
		CachingConnectionFactory cachingConnectionFactory=new CachingConnectionFactory(rabbitConnectionFactoryBean.getObject());
		return cachingConnectionFactory;
	}
	@ConditionalOnProperty(prefix="dlxy.config",name="amqp-enabled",matchIfMissing=false)
	@Bean
	public RabbitTemplate rabbitTemplate() throws Exception
	{
		RabbitTemplate rabbitTemplate=new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory());
		rabbitTemplate.setExchange("dlxy");
		rabbitTemplate.setEncoding("UTF-8");
//		rabbitTemplate.setMessageConverter(messageConverter);
		return rabbitTemplate;
	}
	@ConditionalOnProperty(prefix="dlxy.config",name="amqp-enabled",matchIfMissing=false)
	@Bean
	public RabbitAdmin rabbitAdmin() throws Exception
	{
		RabbitAdmin rabbitAdmin=new RabbitAdmin(connectionFactory());
		rabbitAdmin.declareExchange(dlxyExchange());
		return rabbitAdmin;
	}
	@ConditionalOnProperty(prefix="dlxy.config",name="amqp-enabled",matchIfMissing=false)
	@Bean
	public TopicExchange dlxyExchange()
	{
		return new TopicExchange("dlxy");
	}
	
	@ConditionalOnProperty(prefix="dlxy.config",name="amqp-enabled",matchIfMissing=false)
	@Bean
	public Queue userRecordQueue()
	{
		return new Queue("UserRecordLog");
	}
	@ConditionalOnProperty(prefix="dlxy.config",name="amqp-enabled",matchIfMissing=false)
	@Bean
	public Binding userRecordBinding()
	{
		Queue queue=userRecordQueue();
		return BindingBuilder.bind(queue).to(dlxyExchange()).with(Events.UserRecordLog.name().toUpperCase());
	}
	@ConditionalOnProperty(prefix="dlxy.config",name="amqp-enabled",matchIfMissing=false)
	@Bean
	public Queue userIllegalQueue()
	{
		return new Queue(Events.UserIllegalLog.name());
	}
	@ConditionalOnProperty(prefix="dlxy.config",name="amqp-enabled",matchIfMissing=false)
	@Bean
	public Binding userIllegalBinding()
	{
		Queue queue=userIllegalQueue();
		return BindingBuilder.bind(queue).to(dlxyExchange()).with(Events.UserIllegalLog.name().toUpperCase());
	}
	@ConditionalOnProperty(prefix="dlxy.config",name="amqp-enabled",matchIfMissing=false)
	@Bean
	public AmqpListener userRecordListener()
	{
		return new UserRecordListener();
	}
	@ConditionalOnProperty(prefix="dlxy.config",name="amqp-enabled",matchIfMissing=false)
	@Bean
	public UserIllegalLogListener userIllegalLogListener()
	{
		return new UserIllegalLogListener();
	}
	
	@ConditionalOnProperty(prefix="dlxy.config",name="amqp-enabled",matchIfMissing=false)
	@Bean
	public SimpleMessageListenerContainer simpleMessageListenerContainer(List<? extends AmqpListener> listeners) throws Exception
	{
		FacadedAmqpListener facadedAmqpListener=new FacadedAmqpListener(listeners);
		SimpleMessageListenerContainer simpleMessageListenerContainer=new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
		Set<String>queueNames=new HashSet<>();
		for (AmqpListener amqpListener : listeners)
		{
			queueNames.add(amqpListener.getQueueName());
		}
		String[] array = queueNames.toArray(new String[0]);
		System.out.println(array);
//		simpleMessageListenerContainer.setMessageConverter(messageConverter);
		simpleMessageListenerContainer.setQueueNames(queueNames.toArray(new String[0]));
		simpleMessageListenerContainer.setMaxConcurrentConsumers(3);
		simpleMessageListenerContainer.setConcurrentConsumers(2);
		MessageListenerAdapter adapter=new MessageListenerAdapter();
		adapter.setDelegate(facadedAmqpListener);
		adapter.setDefaultListenerMethod("process");
		simpleMessageListenerContainer.setMessageListener(adapter);
		return simpleMessageListenerContainer;
	}
}
