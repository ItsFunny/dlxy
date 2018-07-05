/**
*
* @author joker 
* @date 创建时间：2018年6月8日 下午2:30:56
* 
*/
package com.dlxy.system.management.config;

import java.io.IOException;
import java.util.List;
import java.util.Observer;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.dlxy.common.service.IdWorkerService;
import com.dlxy.common.service.IdWorkerServiceTwitter;
import com.dlxy.system.management.config.property.DlxyProperty;
import com.dlxy.system.management.config.property.DlxyPropertyPlaceholderConfigurer;
import com.dlxy.system.management.interceptor.RequestIntercept;
import com.dlxy.system.management.service.IArticleManagementWrappedService;
import com.dlxy.system.management.service.IPictureManagementWrappedService;
import com.dlxy.system.management.service.ManagementUserRecordObserver;
import com.dlxy.system.management.service.command.AddOrUpdateArtilceCommand;
import com.dlxy.system.management.service.command.ArticleGroup;
import com.dlxy.system.management.service.command.PictureGroup;
import com.dlxy.system.management.service.command.UserArticleGroup;
import com.dlxy.system.management.service.impl.ManagementArticleServiceObservableImpl;
import com.dlxy.system.management.service.impl.ManagementPictureServiceObservableImpl;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
* 
* @author joker 
* @date 创建时间：2018年6月8日 下午2:30:56
*/

@Configuration
@ComponentScan(basePackages= {
		"com.dlxy"
})
@MapperScan(basePackages= {
	"com.dlxy.server.user.dao.mybatis",
	"com.dlxy.server.article.dao.mybatis",
	"com.dlxy.server.picture.dao.mybatis"
})
@ImportResource(locations= {
		"classpath:/spring/applicationContext.xml"
})
@EnableWebMvc
@EnableTransactionManagement
public class ManagementSystemConfiguration implements WebMvcConfigurer
{

	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(DlxyProperty dlxyProperty)
	{
		DataSourceTransactionManager dataSourceTransactionManager=new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource(dlxyProperty));
		return dataSourceTransactionManager;
	}
	/*
	 * 后期更换为工厂模式,太烦了,总是一样的步骤
	 */
	@Bean
	public AddOrUpdateArtilceCommand addArtilceCommand(List<Observer>observers)
	{
		AddOrUpdateArtilceCommand addArtilceCommand=new AddOrUpdateArtilceCommand();
		for (Observer observer : observers)
		{
			addArtilceCommand.addObserver(observer);
		}
		return addArtilceCommand;
	}
	@Bean
	public IArticleManagementWrappedService articleManagementWrappedservice(List<Observer>observers)
	{
		ManagementArticleServiceObservableImpl managementArticleServiceObservableImpl=new ManagementArticleServiceObservableImpl();
		for (Observer observer : observers)
		{
			managementArticleServiceObservableImpl.addObserver(observer);
		}
		return managementArticleServiceObservableImpl;
	}
	@Bean
	public IPictureManagementWrappedService pictureManagementWrappedService(List<Observer>observers)
	{
		ManagementPictureServiceObservableImpl pictureManagementWrappedService=new ManagementPictureServiceObservableImpl();
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
	public DlxyPropertyPlaceholderConfigurer dlxyPropertyPlaceholderConfigurer() throws IOException
	{
		DlxyPropertyPlaceholderConfigurer propertyPlaceholderConfigurer=new DlxyPropertyPlaceholderConfigurer();
		propertyPlaceholderConfigurer.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath:*.properties"));
		return propertyPlaceholderConfigurer;
	}
	@Bean
	public IdWorkerService IdWorkerService()
	{
		return new IdWorkerServiceTwitter(0,1);
	}
	
	@Bean
	public MultipartResolver multipartResolver()
	{
		CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver();
		commonsMultipartResolver.setMaxInMemorySize(1000000);
		return commonsMultipartResolver;
	}
	@Bean
	public JedisPool jedisPool()
	{
		JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(5);
		jedisPoolConfig.setMaxTotal(1024);
		jedisPoolConfig.setMaxWaitMillis(10000);
		jedisPoolConfig.setTestOnBorrow(true);
//		JedisPool jedisPool=new JedisPool(jedisPoolConfig,dlxyProperty.getRedisHost(),dlxyProperty.getRedisPort(),10000,dlxyProperty.getRedisPassword());
		JedisPool jedisPool=new JedisPool("localhost", 6379);
		return jedisPool;
	}
	@Bean
	public DataSource dataSource(DlxyProperty dlxyProperty)
	{
		DruidDataSource dataSource=new DruidDataSource();
		dataSource.setUsername(dlxyProperty.getUsername());
		dataSource.setDriverClassName(dlxyProperty.getDriverClassName());
		dataSource.setPassword(dlxyProperty.getPassword());
		dataSource.setUrl(dlxyProperty.getUrl());
		return dataSource;
	}
	
	@Bean
	public QueryRunner queryRunner(DlxyProperty dlxyProperty)
	{
		return new QueryRunner(dataSource(dlxyProperty));
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactoryBean(DlxyProperty dlxyProperty) throws IOException
	{
		SqlSessionFactoryBean sessionFactoryBean=new SqlSessionFactoryBean();
		org.apache.ibatis.session.Configuration configuration=new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		sessionFactoryBean.setConfiguration(configuration);
		sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
		sessionFactoryBean.setDataSource(dataSource(dlxyProperty));
		return sessionFactoryBean;
	}
//	@Bean
//	public MapperScannerConfigurer mapperScannerConfigurer()
//	{
//		MapperScannerConfigurer mapperScannerConfigurer=new MapperScannerConfigurer();
//		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSession");
//		mapperScannerConfigurer.setBasePackage("com.dlxy.system");
//		return mapperScannerConfigurer;
//	}
	@Bean
	public ViewResolver viewResolver()
	{
		FreeMarkerViewResolver viewResolver=new FreeMarkerViewResolver();
		viewResolver.setSuffix(".html");
		Properties props=new Properties();
		props.setProperty("contentType", "text/html;charset=utf-8");
		viewResolver.setAttributes(props);
		return viewResolver;
	}
	
	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer()
	{
		FreeMarkerConfigurer freeMarkerConfigurer=new FreeMarkerConfigurer();
//		freemarker.template.Configuration configuration=new freemarker.template.Configuration();
//		configuration.setNumberFormat("0.##");
//		freeMarkerConfigurer.setConfiguration(configuration);
		freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/templates/");
		return freeMarkerConfigurer;
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		registry.addInterceptor(new RequestIntercept()).addPathPatterns("/**");
	}
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
	{
		converters.add(mappingJackson2HttpMessageConverter());
	}
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
	{
		configurer.mediaType("html", MediaType.APPLICATION_JSON);
	}
	@Override
	public void addFormatters(FormatterRegistry registry)
	{
		WebMvcConfigurer.super.addFormatters(registry);
	}
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter()
	{
		return new MappingJackson2HttpMessageConverter();
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
	
	
	
}
