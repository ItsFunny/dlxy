/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午3:21:53
* 
*/
package com.dlxy.system.batch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月29日 下午3:21:53
 */
@ConfigurationProperties(prefix = "dlxy.config")
public class DlxyProperty
{
	private String dbUrl;
	private String dbUsername;
	private String dbPassword;
	private String dbDriverClassName = "com.mysql.jdbc.Driver";

	private String amqpHost;
	private String amqpUsername;
	private String amqpPassword;
	
	private boolean amqpEnabled;
	

	public String getAmqpHost()
	{
		return amqpHost;
	}

	public void setAmqpHost(String amqpHost)
	{
		this.amqpHost = amqpHost;
	}

	public String getAmqpUsername()
	{
		return amqpUsername;
	}

	public void setAmqpUsername(String amqpUsername)
	{
		this.amqpUsername = amqpUsername;
	}

	public String getAmqpPassword()
	{
		return amqpPassword;
	}

	public void setAmqpPassword(String amqpPassword)
	{
		this.amqpPassword = amqpPassword;
	}

	public String getDbUsername()
	{
		return dbUsername;
	}

	public void setDbUsername(String dbUsername)
	{
		this.dbUsername = dbUsername;
	}

	public String getDbPassword()
	{
		return dbPassword;
	}

	public void setDbPassword(String dbPassword)
	{
		this.dbPassword = dbPassword;
	}


	public String getDbUrl()
	{
		return dbUrl;
	}

	public void setDbUrl(String dbUrl)
	{
		this.dbUrl = dbUrl;
	}

	public String getDbDriverClassName()
	{
		return dbDriverClassName;
	}

	public void setDbDriverClassName(String dbDriverClassName)
	{
		this.dbDriverClassName = dbDriverClassName;
	}

	public boolean isAmqpEnabled()
	{
		return amqpEnabled;
	}

	public void setAmqpEnabled(boolean amqpEnabled)
	{
		this.amqpEnabled = amqpEnabled;
	}


}
