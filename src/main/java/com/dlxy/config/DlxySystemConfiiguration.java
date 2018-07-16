package com.dlxy.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.dlxy.common.event.AppEventLogPubliher;
import com.dlxy.common.event.AppEventPublisher;
import com.dlxy.common.event.AppEventRabbitMQPublisher;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月14日 下午6:23:19
 */
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages =
{ "com.dlxy" }, excludeFilters =
{ @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Mapper.class) })
@MapperScan(annotationClass = Mapper.class, basePackages =
{ "com.dlxy" })
public class DlxySystemConfiiguration implements WebMvcConfigurer
{
	@Autowired
	private DlxyProperty dlxyProperty;

	@Profile("dev")
	@Bean
	public DataSource dataSource(DlxyProperty dlxyProperty)
	{
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUsername(dlxyProperty.getUsername());
		dataSource.setPassword(dlxyProperty.getPassword());
		dataSource.setUrl(dataSource.getUrl());
		dataSource.setDriverClassName(dlxyProperty.getDriverClassName());
		return dataSource;
	}

	@Profile("pro")
	@Bean
	public DataSource dataSourcePro(DlxyProperty dlxyProperty)
	{
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUsername(dlxyProperty.getUsername());
		dataSource.setPassword(dlxyProperty.getPassword());
		dataSource.setUrl(dlxyProperty.getUrl());
		dataSource.setDriverClassName(dlxyProperty.getDriverClassName());
		return dataSource;
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactoryBean(DlxyProperty dlxyProperty) throws IOException
	{
		SqlSessionFactoryBean sessionFactoryBeanqBean = new SqlSessionFactoryBean();
		sessionFactoryBeanqBean.setDataSource(dataSource(dlxyProperty));
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		sessionFactoryBeanqBean.setConfiguration(configuration);
		sessionFactoryBeanqBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/*.xml"));
		return sessionFactoryBeanqBean;
	}
//	@Bean
//	public MapperScannerConfigurer mapperScannerConfigurer()
//	{
//		MapperScannerConfigurer configurer=new MapperScannerConfigurer();
//		configurer.setSqlSessionFactoryBeanName("sqlSession");
//		return configurer;
//	}

	@Bean
	public QueryRunner queryRunner(DlxyProperty dlxyProperty)
	{
		return new QueryRunner(dataSource(dlxyProperty));
	}

	/*
	 * view
	 */
	@Bean
	public ViewResolver viewResolver()
	{
		FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
		viewResolver.setSuffix(".html");
		return viewResolver;
	}

	@Bean
	public FreeMarkerConfigurer freemarkerConfigurert()
	{
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/templates/");
		freeMarkerConfigurer.setDefaultEncoding("UTF-8");
		return freeMarkerConfigurer;
	}

	/*
	 * RabbitmQ
	 */
	@Bean
	public ConnectionFactory connectionFactory() throws Exception
	{
		RabbitConnectionFactoryBean rabbitConnectionFactoryBean = new RabbitConnectionFactoryBean();
		rabbitConnectionFactoryBean.setHost(dlxyProperty.getAmqpHost());
		rabbitConnectionFactoryBean.setUsername(dlxyProperty.getAmqpUsername());
		rabbitConnectionFactoryBean.setPassword(dlxyProperty.getAmqpPassword());
		rabbitConnectionFactoryBean.afterPropertiesSet();
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(
				rabbitConnectionFactoryBean.getObject());
		return cachingConnectionFactory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() throws Exception
	{
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory());
		// rabbitTemplate.setMessageConverter(messageConverter);
		return rabbitTemplate;
	}

	@Bean
	public AppEventPublisher appEventPublisher()
	{
		if (dlxyProperty.isAmqpEnabled())
		{
			return new AppEventRabbitMQPublisher();
		} else
		{
			return new AppEventLogPubliher();
		}

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		registry.addResourceHandler("/js/**").addResourceLocations("/static/js/**").setCachePeriod(1056000);
		registry.addResourceHandler("/css/**").addResourceLocations("/static/css/**").setCachePeriod(1056000);
		registry.addResourceHandler("/imgs/**").addResourceLocations("/static/imgs/**").setCachePeriod(1056000);

	}

}
