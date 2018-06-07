/**
*
* @author joker 
* @date 创建时间：2018年6月7日 下午12:30:04
* 
*/
package com.dlxy.manager.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

/**
* 
* @author joker 
* @date 创建时间：2018年6月7日 下午12:30:04
*/
@Configuration
public class DlxyManagerConfig
{
	@Bean
	public DataSource dataSource()
	{
		DruidDataSource dataSource=new DruidDataSource();
		
		return dataSource;
	}

}
