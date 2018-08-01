package com.dlxy.config;


import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.dlxy.common.event.AppEventLogPubliher;
import com.dlxy.common.event.AppEventPublisher;
import com.dlxy.common.event.AppEventRabbitMQPublisher;
import com.dlxy.filter.CharsetFilter;
import com.dlxy.interceptor.IPInterceptor;
import com.dlxy.service.IArticleManagementWrappedService;
import com.dlxy.service.IPictureManagementWrappedService;
import com.dlxy.service.IRedisService;
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
import com.dlxy.service.impl.RedisServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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
@Import(DlxySystemSpringConfiguration.class)
@Order(2)
public class DlxySystemConfiiguration implements WebMvcConfigurer
{
	@Autowired
	private DlxyProperty dlxyProperty;
	
	private Logger logger=LoggerFactory.getLogger(DlxySystemConfiiguration.class);	
//	
	
	@Bean("charsetFilter")
	public CharsetFilter charSetfilter()
	{
		return new CharsetFilter();
	}
	@Bean
	public JedisPool jedisPool()
	{
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(5);
		jedisPoolConfig.setMaxTotal(1024);
		jedisPoolConfig.setMaxWaitMillis(10000);
		jedisPoolConfig.setTestOnBorrow(true);
		// JedisPool jedisPool=new
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, dlxyProperty.getRedisHost(), dlxyProperty.getRedisPort(),
				10000, dlxyProperty.getRedisPassword());
		// JedisPool jedisPool = new JedisPool("localhost", 6379);
		return jedisPool;
	}

	@Bean
	public IRedisService redisService()
	{
		return new  RedisServiceImpl();
	}
	@Bean
	public CommonsMultipartResolver multipartResolver()
	{
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver();
		multipartResolver.setMaxInMemorySize(100000);
		return multipartResolver;
	}
	
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
		rabbitTemplate.setExchange("dlxy");
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
		registry.addResourceHandler("/public/404.html").addResourceLocations("/WEB-INF/templates/404.html").setCachePeriod(1056000);
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
	{
		configurer.mediaType("html", MediaType.APPLICATION_JSON_UTF8);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
	{
		MappingJackson2HttpMessageConverter jackson2HttpMessageConverter=new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper=new ObjectMapper();
		SimpleModule simpleModule=new SimpleModule();
		simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		objectMapper.registerModule(simpleModule);
		jackson2HttpMessageConverter.setObjectMapper(objectMapper);
		converters.add(jackson2HttpMessageConverter);
		converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
	}
	
	@PostConstruct
	public void init() throws IOException
	{
		dlxyProperty.init();
		logger.info("{}",dlxyProperty);
	}
	@Bean
	public IPInterceptor ipInterceptor()
	{
		return new IPInterceptor();
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
//		registry.addInterceptor(ipInterceptor()).addPathPatterns("/*").excludePathPatterns("/public/*");
		registry.addInterceptor(ipInterceptor()).excludePathPatterns("/public/**","/api/**","/admin/**").addPathPatterns("/**");
	}
	@Autowired
	public void setDlxyProperty(DlxyProperty dlxyProperty)
	{
		this.dlxyProperty = dlxyProperty;
	}

}
