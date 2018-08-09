package com.dlxy.config;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
import com.dlxy.interceptor.IPInterceptor;
import com.dlxy.service.IRedisService;
import com.dlxy.service.command.ArticleReceiver;
import com.dlxy.service.command.PictureReceiver;
import com.dlxy.service.command.UserArticleReceiver;
import com.dlxy.service.impl.RedisServiceImpl;
import com.dlxy.strategy.DefaultFileService;
import com.dlxy.strategy.FTPFileService;
import com.dlxy.strategy.FileStrategyContext;
import com.dlxy.strategy.IFileStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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

	private Logger logger = LoggerFactory.getLogger(DlxySystemConfiiguration.class);

	
	
	@Bean
	public FileStrategyContext fileContext()
	{
		Map<String, String>pathMap=new HashMap<String, String>();
		Map<String, String>visitPrefixMap=new HashMap<>();
		FileStrategyContext context=new FileStrategyContext();
		
//		DefaultFileService defaultFileService=new DefaultFileService();
//		defaultFileService.setVisitPrefix("imgs");
//		pathMap.put(IFileStrategy.IMG_TYPE, "/");
//		visitPrefixMap.put(IFileStrategy.IMG_TYPE, "imgs");
//		defaultFileService.setVisitPrefixMap(visitPrefixMap);
//		defaultFileService.setBasePathMap(pathMap);
//		context.setFileStrategy(defaultFileService);
		
		
		pathMap.put(IFileStrategy.IMG_TYPE, "/home/joker/www");
		visitPrefixMap.put(IFileStrategy.IMG_TYPE, "imgs");
		String host="120.78.240.211";
		Integer port=21;
		String username="joker";
		String password="lvcong124536789";
		FTPFileService fileService=new FTPFileService(host, port, username, password);
		fileService.setVisitPrefixMap(visitPrefixMap);
		fileService.setBasePathMap(pathMap);
		context.setFileStrategy(fileService);
		return context;
	}
	
	
	
	@Bean
	public JedisPool jedisPool()
	{
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(5);
		jedisPoolConfig.setMaxTotal(1024);
		jedisPoolConfig.setMaxWaitMillis(10000);
		jedisPoolConfig.setTestOnBorrow(true);
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, dlxyProperty.getRedisHost(), dlxyProperty.getRedisPort(),
				10000, dlxyProperty.getRedisPassword());

		// JedisPool jedisPool = new JedisPool("localhost", 6379);
		return jedisPool;
	}

	

	@Bean
	public IRedisService redisService()
	{
		return new RedisServiceImpl();
	}

	@Bean
	public CommonsMultipartResolver multipartResolver()
	{
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxInMemorySize(100000);
		return multipartResolver;
	}

	@Bean
	public DataSourceTransactionManager DataSourceTransactionManager()
	{
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource());
		return dataSourceTransactionManager;
	}

	@Bean
	public PictureReceiver pictureGroup()
	{
		return new PictureReceiver();
	}

	@Bean
	public ArticleReceiver articleGroup()
	{
		return new ArticleReceiver();
	}

	@Bean
	public UserArticleReceiver userArticleGroup()
	{
		return new UserArticleReceiver();
	}

	@Bean
	public DataSource dataSource()
	{
		DruidDataSource dataSource = new DruidDataSource();

		dataSource.setTestOnBorrow(false);
		dataSource.setTestWhileIdle(true);
		dataSource.setTimeBetweenEvictionRunsMillis(10000);
		dataSource.setValidationQuery("SELECT 1");
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
		registry.addResourceHandler("/public/404.html").addResourceLocations("/WEB-INF/templates/404.html")
				.setCachePeriod(1056000);
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
	{
		configurer.mediaType("html", MediaType.APPLICATION_JSON_UTF8);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
	{
		// 如果使用到了json这个消息转换器,则将内部的Long类型,BigInteger类型以String的格式进行转换,使用其他消息转换器的不做此处理
		MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule simpleModule = new SimpleModule();
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
		logger.info("{}", dlxyProperty);
		
	}
	public static void main(String[] args)
	{
		DlxySystemConfiiguration dlxySystemConfiiguration=new DlxySystemConfiiguration();
		URL resource = dlxySystemConfiiguration.getClass().getClassLoader().getResource("WEB-INF");
		System.out.println(resource.getPath());
	}

	@Bean
	public IPInterceptor ipInterceptor()
	{
		return new IPInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		// registry.addInterceptor(ipInterceptor()).addPathPatterns("/*").excludePathPatterns("/public/*");
		registry.addInterceptor(ipInterceptor()).excludePathPatterns("/public/**", "/api/**", "/admin/**")
				.addPathPatterns("/**");
	}

	@Autowired
	public void setDlxyProperty(DlxyProperty dlxyProperty)
	{
		this.dlxyProperty = dlxyProperty;
	}

}
