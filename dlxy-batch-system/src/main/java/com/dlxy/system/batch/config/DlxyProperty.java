/**
*
* @author joker 
* @date 创建时间：2018年6月7日 下午12:50:25
* 
*/
package com.dlxy.system.batch.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author joker
 * @date 创建时间：2018年6月7日 下午12:50:25
 */
@Component
@ConfigurationProperties(prefix="dlxy.config")
public class DlxyProperty implements InitializingBean
{
	private String dbUsername;
	private String dbPassword;
	private String driverClassName = "com.mysql.jdbc.Driver";
	private String dbUrl;

	/*
	 * rabbitMQ
	 */
	private String amqpHost;
	private String amqpUsername;
	private String amqpPassword;
	private boolean amqpEnabled;
	private Integer amqpPort;
	/*
	 * redis
	 */
	private String redisHost;
	private Integer redisPort;
	private String redisPassword;
	
	private long workerId;
	private long datacenterId;
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
	public String getDriverClassName()
	{
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName)
	{
		this.driverClassName = driverClassName;
	}
	public String getDbUrl()
	{
		return dbUrl;
	}
	public void setDbUrl(String dbUrl)
	{
		this.dbUrl = dbUrl;
	}
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
	public boolean isAmqpEnabled()
	{
		return amqpEnabled;
	}
	public void setAmqpEnabled(boolean amqpEnabled)
	{
		this.amqpEnabled = amqpEnabled;
	}
	public Integer getAmqpPort()
	{
		return amqpPort;
	}
	public void setAmqpPort(Integer amqpPort)
	{
		this.amqpPort = amqpPort;
	}
	public String getRedisHost()
	{
		return redisHost;
	}
	public void setRedisHost(String redisHost)
	{
		this.redisHost = redisHost;
	}
	public Integer getRedisPort()
	{
		return redisPort;
	}
	public void setRedisPort(Integer redisPort)
	{
		this.redisPort = redisPort;
	}
	public String getRedisPassword()
	{
		return redisPassword;
	}
	public void setRedisPassword(String redisPassword)
	{
		this.redisPassword = redisPassword;
	}
	public long getWorkerId()
	{
		return workerId;
	}
	public void setWorkerId(long workerId)
	{
		this.workerId = workerId;
	}
	public long getDatacenterId()
	{
		return datacenterId;
	}
	public void setDatacenterId(long datacenterId)
	{
		this.datacenterId = datacenterId;
	}
	@Override
	public void afterPropertiesSet() throws Exception
	{
		
	}
	@Override
	public String toString()
	{
		return "DlxyProperty [dbUsername=" + dbUsername + ", dbPassword=" + dbPassword + ", driverClassName="
				+ driverClassName + ", dbUrl=" + dbUrl + ", amqpHost=" + amqpHost + ", amqpUsername=" + amqpUsername
				+ ", amqpPassword=" + amqpPassword + ", amqpEnabled=" + amqpEnabled + ", amqpPort=" + amqpPort
				+ ", redisHost=" + redisHost + ", redisPort=" + redisPort + ", redisPassword=" + redisPassword
				+ ", workerId=" + workerId + ", datacenterId=" + datacenterId + "]";
	}
	


}
