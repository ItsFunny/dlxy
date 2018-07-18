package com.dlxy.config;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.dlxy.common.event.AppEventLogPubliher;
import com.dlxy.common.event.AppEventPublisher;
import com.dlxy.common.event.AppEventRabbitMQPublisher;
import com.dlxy.common.service.IdWorkerService;
import com.dlxy.common.service.IdWorkerServiceTwitter;
import com.dlxy.service.IArticleManagementWrappedService;
import com.dlxy.service.IPictureManagementWrappedService;
import com.dlxy.service.ITitleManagementWrappedService;
import com.dlxy.service.IUserMangementWrappedService;
import com.dlxy.service.ManagementUserRecordObserver;
import com.dlxy.service.command.AddOrUpdateArtilceCommand;
import com.dlxy.service.command.ArticleGroup;
import com.dlxy.service.command.PictureGroup;
import com.dlxy.service.command.UserArticleGroup;
import com.dlxy.service.impl.ManagemeentTitleServiceImpl;
import com.dlxy.service.impl.ManagementArticleServiceObservableImpl;
import com.dlxy.service.impl.ManagementPictureServiceObservableImpl;
import com.dlxy.service.impl.ManagementUserServiceImpl;

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

	
	@Bean
	public DataSourceTransactionManager DataSourceTransactionManager()
	{
		DataSourceTransactionManager dataSourceTransactionManager=new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource());
		return dataSourceTransactionManager;
	}
	@Bean
	public PictureGroup pictureGroup()
	{
		return new PictureGroup();
	}

	@Bean
	public ArticleGroup articleGroup()
	{
		return new ArticleGroup();
	}

	@Bean
	public UserArticleGroup userArticleGroup()
	{
		return new UserArticleGroup();
	}

	@Bean
	public AddOrUpdateArtilceCommand addArtilceCommand(List<Observer> observers)
	{
		AddOrUpdateArtilceCommand addArtilceCommand = new AddOrUpdateArtilceCommand();
		for (Observer observer : observers)
		{
			addArtilceCommand.addObserver(observer);
		}
		return addArtilceCommand;
	}

	@Bean
	public ITitleManagementWrappedService titleMangementWrappedServie(List<Observer> observers)
	{
		ManagemeentTitleServiceImpl iTitleManagementWrappedService=new ManagemeentTitleServiceImpl();
		for (Observer observer : observers)
		{
			iTitleManagementWrappedService.addObserver(observer);
		}
		return iTitleManagementWrappedService;
	}
	@Bean
	public IUserMangementWrappedService userManagementWrappedservice(List<Observer> observers)
	{
		ManagementUserServiceImpl userServiceImpl = new ManagementUserServiceImpl();
		for (Observer observer : observers)
		{
			userServiceImpl.addObserver(observer);
		}
		return userServiceImpl;
	}

	@Bean
	public IArticleManagementWrappedService articleManagementWrappedservice(List<Observer> observers)
	{
		ManagementArticleServiceObservableImpl managementArticleServiceObservableImpl = new ManagementArticleServiceObservableImpl();
		for (Observer observer : observers)
		{
			managementArticleServiceObservableImpl.addObserver(observer);
		}
		return managementArticleServiceObservableImpl;
	}

	@Bean
	public IPictureManagementWrappedService pictureManagementWrappedService(List<Observer> observers)
	{
		ManagementPictureServiceObservableImpl pictureManagementWrappedService = new ManagementPictureServiceObservableImpl();
		for (Observer observer : observers)
		{
			pictureManagementWrappedService.addObserver(observer);
		}
		return pictureManagementWrappedService;
	}

	@Bean
	public ManagementUserRecordObserver managementUserRecordObserver()
	{
		return new ManagementUserRecordObserver();
	}

	@Bean
	public DataSource dataSource()
	{
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUsername(dlxyProperty.getUsername());
		dataSource.setDriverClassName(dlxyProperty.getDriverClassName());
		dataSource.setPassword(dlxyProperty.getPassword());
		dataSource.setUrl(dlxyProperty.getUrl());
		return dataSource;
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException
	{
		SqlSessionFactoryBean sessionFactoryBeanqBean = new SqlSessionFactoryBean();
		sessionFactoryBeanqBean.setDataSource(dataSource());
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		sessionFactoryBeanqBean.setConfiguration(configuration);
		sessionFactoryBeanqBean
				.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/*.xml"));
		return sessionFactoryBeanqBean;
	}
	// @Bean
	// public MapperScannerConfigurer mapperScannerConfigurer()
	// {
	// MapperScannerConfigurer configurer=new MapperScannerConfigurer();
	// configurer.setSqlSessionFactoryBeanName("sqlSession");
	// return configurer;
	// }

	@Bean
	public QueryRunner queryRunner()
	{
		return new QueryRunner(dataSource());
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

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
	{
		configurer.mediaType("html", MediaType.APPLICATION_JSON_UTF8);
	}

}
