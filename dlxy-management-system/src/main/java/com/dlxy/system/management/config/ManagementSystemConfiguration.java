/**
*
* @author joker 
* @date 创建时间：2018年6月8日 下午2:30:56
* 
*/
package com.dlxy.system.management.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.dlxy.system.management.config.property.DlxyProperty;
import com.dlxy.system.management.config.property.DlxyPropertyPlaceholderConfigurer;

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
	"com.dlxy.server.article.dao.mybatis"
})
public class ManagementSystemConfiguration
{
	@Bean
	public DlxyPropertyPlaceholderConfigurer dlxyPropertyPlaceholderConfigurer() throws IOException
	{
		DlxyPropertyPlaceholderConfigurer propertyPlaceholderConfigurer=new DlxyPropertyPlaceholderConfigurer();
		propertyPlaceholderConfigurer.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath:*.properties"));
		return propertyPlaceholderConfigurer;
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

	@Bean("sqlSession")
	public SqlSessionFactoryBean sqlSessionFactoryBean(DlxyProperty dlxyProperty)
	{
		SqlSessionFactoryBean sessionFactoryBean=new SqlSessionFactoryBean();
		org.apache.ibatis.session.Configuration configuration=new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		sessionFactoryBean.setConfiguration(configuration);
//		sessionFactoryBean.setMapperLocations(mapperLocations);
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
		freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/templates/");
		return freeMarkerConfigurer;
	}
	
}