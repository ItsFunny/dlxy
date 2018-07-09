/**
*
* @author joker 
* @date 创建时间：2018年6月8日 下午2:26:32
* 
*/
package com.dlxy.system.config;


import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.alibaba.druid.pool.DruidDataSource;

/**
* 
* @author joker 
* @date 创建时间：2018年6月8日 下午2:26:32
*/
@Configuration
@EnableWebMvc
@ComponentScan(basePackages= {
		"com.dlxy"
})
@MapperScan(annotationClass= Mapper.class,basePackages =
{ "com.dlxy.server.user.dao.mybatis", "com.dlxy.server.article.dao.mybatis", "com.dlxy.server.picture.dao.mybatis" })
public class PortalSystemConfiguration implements WebMvcConfigurer
{
	
	
	@Profile("dev")
	@Bean
	public DlxyPropertyPlaceholderConfigurer placeholderConfigurerDev() throws IOException
	{
		DlxyPropertyPlaceholderConfigurer dlxyPropertyPlaceholderConfigurer=new DlxyPropertyPlaceholderConfigurer();
		dlxyPropertyPlaceholderConfigurer.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/dev/*.properties"));
		dlxyPropertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
		return dlxyPropertyPlaceholderConfigurer;
	}
	@Profile("pro")
	@Bean
	public DlxyPropertyPlaceholderConfigurer placeholderConfigurerPro() throws IOException
	{
		DlxyPropertyPlaceholderConfigurer dlxyPropertyPlaceholderConfigurer=new DlxyPropertyPlaceholderConfigurer();
		dlxyPropertyPlaceholderConfigurer.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/pro/*.properties"));
		dlxyPropertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
		return dlxyPropertyPlaceholderConfigurer;
	}
	@Bean
	public DataSource dataSource(DlxyProperty dlxyProperty)
	{
		DruidDataSource dataSource=new DruidDataSource();
		dataSource.setUsername(dlxyProperty.getUsername());
		dataSource.setPassword(dlxyProperty.getPassword());
		dataSource.setUrl(dlxyProperty.getUrl());
		dataSource.setDriverClassName(dlxyProperty.getDriverClassName());
		return dataSource;
	}

	@Bean(name="sqlSessionFactory")
	public SqlSessionFactoryBean sqlSessionFactoryBean(DlxyProperty dlxyProperty) throws IOException
	{
		SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource(dlxyProperty));
		org.apache.ibatis.session.Configuration configuration=new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		sqlSessionFactoryBean.setConfiguration(configuration);
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/*.xml"));
		return sqlSessionFactoryBean;
	}
	@Bean
	public QueryRunner queryRunner(DlxyProperty dlxyProperty)
	{
		return new QueryRunner(dataSource(dlxyProperty));
	}
//	@Bean
//	public MapperScannerConfigurer mapperScannerConfigurer()
//	{
//		MapperScannerConfigurer mapperScannerConfigurer=new MapperScannerConfigurer();
//		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
////		mapperScannerConfigurer.set
//		return mapperScannerConfigurer;
//	}
	@Bean
	public ViewResolver viewResolver()
	{
		FreeMarkerViewResolver freeMarkerViewResolver=new FreeMarkerViewResolver();
		freeMarkerViewResolver.setSuffix(".html");
		return freeMarkerViewResolver;
	}
	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer()
	{
		FreeMarkerConfigurer freeMarkerConfigurer=new FreeMarkerConfigurer();
		freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/templates/");
		freeMarkerConfigurer.setDefaultEncoding("UTF-8");
		return freeMarkerConfigurer;
	}
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
//		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js").setCachePeriod(1036000);
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
	{
		converters.add(new MappingJackson2HttpMessageConverter());
	}
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
	{
		configurer.mediaType("html", MediaType.APPLICATION_JSON);
	}
	
	
	
	
}
