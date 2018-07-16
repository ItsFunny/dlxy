/**
*
* @author joker 
* @date 创建时间：2018年6月7日 下午12:50:25
* 
*/
package com.dlxy.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @author joker
 * @date 创建时间：2018年6月7日 下午12:50:25
 */
@Component
public class DlxyProperty implements InitializingBean
{
	@Value("${dlxy.db.username}")
	private String username;
	@Value("${dlxy.db.password}")
	private String password;
	private String driverClassName = "com.mysql.jdbc.Driver";
	@Value("${dlxy.db.url}")
	private String url;

	/*
	 * rabbitMQ
	 */
	@Value("${dlxy.amqp.amqpHost}")
	private String amqpHost;
	@Value("${dlxy.amqp.username}")
	private String amqpUsername;
	@Value("${dlxy.amqp.password}")
	private String amqpPassword;
	@Value("${dlxy.amqp.amqpEnabled}")
	private boolean amqpEnabled;
	@Value("${dlxy.amqp.port}")
	private Integer amqpPort;
	/*
	 * redis
	 */
	@Value("${dlxy.redis.host}")
	private String redisHost;
	@Value("${dlxy.redis.port}")
	private Integer redisPort;
	@Value("${dlxy.redis.password}")
	private String redisPassword;
	
	@Value("${dlxy.workerId}")
	private long workerId;
	@Value("${dlxy.datacenterId}")
	private long datacenterId;
	

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getDriverClassName()
	{
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName)
	{
		this.driverClassName = driverClassName;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		System.out.println(this);
	}

	@Override
	public String toString()
	{
		return "DlxyProperty [username=" + username + ", password=" + password + ", driverClassName=" + driverClassName
				+ ", url=" + url + "]";
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

}
