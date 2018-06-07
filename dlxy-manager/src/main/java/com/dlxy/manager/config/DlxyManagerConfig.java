/**
*
	* @author joker 
* @date 创建时间：2018年6月7日 下午12:30:04
* 
*/
package com.dlxy.manager.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.dlxy.manager.config.property.DlxyProperty;
import com.dlxy.manager.config.property.DlxyPropertyPlaceholderConfigurer;

/**
* 
* @author joker 
* @date 创建时间：2018年6月7日 下午12:30:04
*/
@Configuration
@ComponentScan(basePackages="com.dlxy.manager.controller")
@MapperScan(basePackages="com.dlxy.manager.dao")
public class DlxyManagerConfig
{
//	@Autowired
//	private DlxyProperty dlxyProperty;
	
	@Bean
	public DlxyPropertyPlaceholderConfigurer dlxyPropertyPlaceholderConfigurer()
	{
		return new DlxyPropertyPlaceholderConfigurer();
	}
	@Bean
	public DataSource dataSource(DlxyProperty dlxyProperty)
	{
		DruidDataSource dataSource=new DruidDataSource();
		dataSource.setUsername(dlxyProperty.getUsername());
		dataSource.setPassword(dlxyProperty.getPassword());
		dataSource.setDriverClassName(dlxyProperty.getDriverClassName());
		dataSource.setUrl(dlxyProperty.getUrl());
		return dataSource;
	}
//	@Bean
//	public DataSource dataSource()
//	{
//		DruidDataSource dataSource=new DruidDataSource();
//		dataSource.setUsername("root");
//		dataSource.setPassword("123456");
//		dataSource.setUrl("jdbc:mysql://localhost/jn?characterEncoding=utf-8&useSSL=false");
//		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//		return dataSource;
//	}
	@Bean("sqlSession")
	public SqlSessionFactoryBean sqlSessionFactoryBean(DlxyProperty dlxyProperty) throws IOException
	{
		SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource(dlxyProperty));
		org.apache.ibatis.session.Configuration configuration=new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		sqlSessionFactoryBean.setConfiguration(configuration);
//		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(""));
		return sqlSessionFactoryBean;
	}
//	@Bean
//	public MapperScannerConfigurer mapperScannerConfigurer()
//	{
//		MapperScannerConfigurer mapperScannerConfigurer=new MapperScannerConfigurer();
//		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSession");
//		mapperScannerConfigurer.setBasePackage("com.dlxy.manager.dao");
//		return mapperScannerConfigurer;
//	}
	@Bean
	public ViewResolver viewResolver()
	{
		FreeMarkerViewResolver viewResolver=new FreeMarkerViewResolver();
		viewResolver.setSuffix(".html");
		Properties properties=new Properties();
		properties.setProperty("contentType", "text/html;charSet=utf-8");
		viewResolver.setAttributes(properties);
		return viewResolver;
	}
	@Bean
	public FreeMarkerConfigurer freemarkerConfigurer()
	{
		FreeMarkerConfigurer configurer=new FreeMarkerConfigurer();
		configurer.setTemplateLoaderPath("/WEB-INF/templates/");
		return configurer;
	}
}
