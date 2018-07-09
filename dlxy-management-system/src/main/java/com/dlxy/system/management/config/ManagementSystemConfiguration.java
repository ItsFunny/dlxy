/**
*
* @author joker 
* @date 创建时间：2018年6月8日 下午2:30:56
* 
*/
package com.dlxy.system.management.config;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.alibaba.druid.pool.DruidDataSource;
import com.dlxy.common.service.IdWorkerService;
import com.dlxy.common.service.IdWorkerServiceTwitter;
import com.dlxy.system.management.config.property.DlxyProperty;
import com.dlxy.system.management.config.property.DlxyPropertyPlaceholderConfigurer;
import com.dlxy.system.management.config.shiro.DlxyShiroAuthRealm;
import com.dlxy.system.management.filter.TFilter;
import com.dlxy.system.management.interceptor.RequestIntercept;
import com.dlxy.system.management.service.IArticleManagementWrappedService;
import com.dlxy.system.management.service.IPictureManagementWrappedService;
import com.dlxy.system.management.service.IUserMangementWrappedService;
import com.dlxy.system.management.service.ManagementUserRecordObserver;
import com.dlxy.system.management.service.command.AddOrUpdateArtilceCommand;
import com.dlxy.system.management.service.command.ArticleGroup;
import com.dlxy.system.management.service.command.PictureGroup;
import com.dlxy.system.management.service.command.UserArticleGroup;
import com.dlxy.system.management.service.impl.ManagementArticleServiceObservableImpl;
import com.dlxy.system.management.service.impl.ManagementPictureServiceObservableImpl;
import com.dlxy.system.management.service.impl.ManagementUserServiceImpl;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author joker
 * @date 创建时间：2018年6月8日 下午2:30:56
 */

@Configuration
@ComponentScan(basePackages =
{ "com.dlxy" },excludeFilters= {
		@ComponentScan.Filter(type=FilterType.ANNOTATION,value={
				
		})
})
@MapperScan(annotationClass= Mapper.class,basePackages =
{ "com.dlxy.server.user.dao.mybatis", "com.dlxy.server.article.dao.mybatis", "com.dlxy.server.picture.dao.mybatis" })
@EnableWebMvc
@EnableTransactionManagement
public class ManagementSystemConfiguration implements WebMvcConfigurer
{

	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(DlxyProperty dlxyProperty)
	{
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource(dlxyProperty));
		return dataSourceTransactionManager;
	}

	/*
	 * 后期更换为工厂模式,太烦了,总是一样的步骤
	 */
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

	@Profile("dev")
	@Bean
	public DlxyPropertyPlaceholderConfigurer dlxyPropertyPlaceholderConfigurer() throws IOException
	{
		DlxyPropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new DlxyPropertyPlaceholderConfigurer();
		propertyPlaceholderConfigurer
				.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/dev/*.properties"));
		return propertyPlaceholderConfigurer;
	}
	@Profile("pro")
	@Bean
	public DlxyPropertyPlaceholderConfigurer dlxyPropertyProPlaceholderConfigurer() throws IOException
	{
		DlxyPropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new DlxyPropertyPlaceholderConfigurer();
		propertyPlaceholderConfigurer
				.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/pro/*.properties"));
		return propertyPlaceholderConfigurer;
	}
	//shiro
	@Bean
	public DlxyShiroAuthRealm dlxyShiroAuthRealm()
	{
		return new DlxyShiroAuthRealm();
	}
	@Bean
	public SecurityManager securityManager()
	{
		DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
		securityManager.setRealm(dlxyShiroAuthRealm());
		return securityManager;
	}
	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean()
	{
		ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager());
		shiroFilterFactoryBean.setLoginUrl("/login.html");
		shiroFilterFactoryBean.setSuccessUrl("/index.html");
		shiroFilterFactoryBean.setUnauthorizedUrl("/public/unauth.html");
//		Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
//		filters.put("authc", new TFilter());
		Map<String, String> filterChainDefinitionMap = shiroFilterFactoryBean.getFilterChainDefinitionMap();
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/common/**", "anon");
		filterChainDefinitionMap.put("/imgs/**", "anon");
		filterChainDefinitionMap.put("/images/**", "anon");
		filterChainDefinitionMap.put("/jsplug/**", "anon");
		filterChainDefinitionMap.put("/lang/**", "anon");
		filterChainDefinitionMap.put("/public/**", "anon");
		filterChainDefinitionMap.put("/plugins/**", "anon");
		filterChainDefinitionMap.put("/portal/**", "anon");
		filterChainDefinitionMap.put("/themes", "anon");
		filterChainDefinitionMap.put("/kindeditor-all.js", "anon");
		filterChainDefinitionMap.put("/kindeditor-all-min.js", "anon");
//		filterChainDefinitionMap.put("/api/**", "authc");
//		filterChainDefinitionMap.put("/user/**","authc");
		filterChainDefinitionMap.put("/**", "authc");
		filterChainDefinitionMap.put("/public/doLogin.html", "authc");
	
		return shiroFilterFactoryBean;
	}
//	@Bean
//	public SecurityManager securityManager()
//	{
//		DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
//		securityManager.setRealm(managementAuthorizingRealm());
//		return securityManager;
//	}
//	@Bean(name="shiroFilter")
//	public ShiroFilterFactoryBean shiroFilterFactoryBean()
//	{
//		ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
//		shiroFilterFactoryBean.setSecurityManager(securityManager());
//		shiroFilterFactoryBean.setLoginUrl("www.baidu.com");
//		Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
//		filters.put("authc", new TempFilter());
//		Map<String, String> filterChainDefinitionMap = shiroFilterFactoryBean.getFilterChainDefinitionMap();
//		filterChainDefinitionMap.put("/test", "anon");
//		filterChainDefinitionMap.put("/index", "anon");
//		return shiroFilterFactoryBean;
//	}
	@Bean(name="lifeCycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor()
	{
		return new LifecycleBeanPostProcessor();
	}
//	/*
//	 * 将securityManger绑定
//	 */
//	@Bean
//	public MethodInvokingFactoryBean methodInvokingFactoryBean()
//	{
//		MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
//		factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
//		factoryBean.setArguments(securityManager());
//		return factoryBean;
//	}
//	/*
//	 * 开启注解支持
//	 */
	@DependsOn(value= {"lifeCycleBeanPostProcessor"})
	@Bean(name = "defaultAdvisorAutoProxyCreator")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator()
	{
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}
//
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager)
	{
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
	@Bean
	public Producer kaptcha()
	{
		Properties p = new Properties();
		p.setProperty(Constants.KAPTCHA_BORDER, "no");
		p.setProperty(Constants.KAPTCHA_BORDER_COLOR, "105,179,90");
		p.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "14,78,149");
		p.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "487");
		p.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "68");
		p.setProperty(Constants.KAPTCHA_SESSION_KEY, "kcode");
		p.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
		p.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "宋体,楷体,微软雅黑");

		DefaultKaptcha kaptcha = new DefaultKaptcha();
		kaptcha.setConfig(new Config(p));
		return kaptcha;
	}

	@Bean
	public IdWorkerService IdWorkerService()
	{
		return new IdWorkerServiceTwitter(0, 1);
	}

	@Bean
	public MultipartResolver multipartResolver()
	{
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setMaxInMemorySize(1000000);
		return commonsMultipartResolver;
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
		// JedisPool(jedisPoolConfig,dlxyProperty.getRedisHost(),dlxyProperty.getRedisPort(),10000,dlxyProperty.getRedisPassword());
		JedisPool jedisPool = new JedisPool("localhost", 6379);
		return jedisPool;
	}

	@Bean
	public DataSource dataSource(DlxyProperty dlxyProperty)
	{
		DruidDataSource dataSource = new DruidDataSource();
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
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		sessionFactoryBean.setConfiguration(configuration);
		sessionFactoryBean
				.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
		sessionFactoryBean.setDataSource(dataSource(dlxyProperty));
		return sessionFactoryBean;
	}

	// @Bean
	// public MapperScannerConfigurer mapperScannerConfigurer()
	// {
	// MapperScannerConfigurer mapperScannerConfigurer=new
	// MapperScannerConfigurer();
	// mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSession");
	// mapperScannerConfigurer.setBasePackage("com.dlxy.system");
	// return mapperScannerConfigurer;
	// }
	@Bean
	public ViewResolver viewResolver()
	{
		FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
		viewResolver.setSuffix(".html");
		Properties props = new Properties();
		props.setProperty("contentType", "text/html;charset=utf-8");
		viewResolver.setAttributes(props);
		return viewResolver;
	}

	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer()
	{
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		// freemarker.template.Configuration configuration=new
		// freemarker.template.Configuration();
		// configuration.setNumberFormat("0.##");
		// freeMarkerConfigurer.setConfiguration(configuration);
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
