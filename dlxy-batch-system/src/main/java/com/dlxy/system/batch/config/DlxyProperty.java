/**
*
* @author joker 
* @date 创建时间：2018年6月7日 下午12:50:25
* 
*/
package com.dlxy.system.batch.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
//    ftp-host: 120.78.240.211
//    ftp-port: 21
//    ftp-username: joker
//    ftp-password: lvcong124536789
//    imgFTPStoreBasePath: /home/joker/www
//    imgFTPVisitPrefx: imgs
//    #local
//    imgLocalVisitPrefix: imgs
	//ftp
	private String ftpHost;
	private Integer ftpPort;
	private String ftpUsername;
	private String ftpPassword;
	private String imgFTPStoreBasePath;
	private String imgFTPVisitPrefx;
	private String imgLocalVisitPrefix;
	
	
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
	
	private String propertyPublicKeyPath;
	private byte[] publicKeyBytes;
	
	
	/*
	 * batch
	 */
	private String[] batchClasses;
	
	
	public void init() throws IOException
	{
		loadPublicKey();
	}
	public void loadPublicKey() throws IOException
	{
		if(null!=publicKeyBytes)
		{
			return;
		}
		if(StringUtils.isEmpty(propertyPublicKeyPath))
		{
			return;
		}
		PathMatchingResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource(propertyPublicKeyPath);
		InputStream inputStream=null;
		try
		{
			inputStream=resource.getInputStream();
			int index = 0;
			StringBuilder sb = new StringBuilder();
			while ((index = inputStream.read()) != -1)
			{
				sb.append((char) index);
			}
//			publicKeyBytes=Base64.getDecoder().decode(sb.toString());
			this.publicKeyBytes=sb.toString().getBytes();
		} catch (Exception e)
		{
			e.printStackTrace();
		}finally {
			inputStream.close();
		}
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
	
	public String getPropertyPublicKeyPath()
	{
		return propertyPublicKeyPath;
	}
	public void setPropertyPublicKeyPath(String propertyPublicKeyPath)
	{
		this.propertyPublicKeyPath = propertyPublicKeyPath;
	}
	public byte[] getPublicKeyBytes()
	{
		return publicKeyBytes;
	}
	public void setPublicKeyBytes(byte[] publicKeyBytes)
	{
		this.publicKeyBytes = publicKeyBytes;
	}
	@Override
	public String toString()
	{
		return "DlxyProperty [dbUsername=" + dbUsername + ", dbPassword=" + dbPassword + ", driverClassName="
				+ driverClassName + ", dbUrl=" + dbUrl + ", ftpHost=" + ftpHost + ", ftpPort=" + ftpPort
				+ ", ftpUsername=" + ftpUsername + ", ftpPassword=" + ftpPassword + ", imgFTPStoreBasePath="
				+ imgFTPStoreBasePath + ", imgFTPVisitPrefx=" + imgFTPVisitPrefx + ", imgLocalVisitPrefix="
				+ imgLocalVisitPrefix + ", amqpHost=" + amqpHost + ", amqpUsername=" + amqpUsername + ", amqpPassword="
				+ amqpPassword + ", amqpEnabled=" + amqpEnabled + ", amqpPort=" + amqpPort + ", redisHost=" + redisHost
				+ ", redisPort=" + redisPort + ", redisPassword=" + redisPassword + ", workerId=" + workerId
				+ ", datacenterId=" + datacenterId + ", propertyPublicKeyPath=" + propertyPublicKeyPath
				+ ", publicKeyBytes=" + Arrays.toString(publicKeyBytes) + ", batchClasses="
				+ Arrays.toString(batchClasses) + "]";
	}
	public String[] getBatchClasses()
	{
		return batchClasses;
	}
	public void setBatchClasses(String[] batchClasses)
	{
		this.batchClasses = batchClasses;
	}
	public String getFtpHost()
	{
		return ftpHost;
	}
	public void setFtpHost(String ftpHost)
	{
		this.ftpHost = ftpHost;
	}
	public Integer getFtpPort()
	{
		return ftpPort;
	}
	public void setFtpPort(Integer ftpPort)
	{
		this.ftpPort = ftpPort;
	}
	public String getFtpUsername()
	{
		return ftpUsername;
	}
	public void setFtpUsername(String ftpUsername)
	{
		this.ftpUsername = ftpUsername;
	}
	public String getFtpPassword()
	{
		return ftpPassword;
	}
	public void setFtpPassword(String ftpPassword)
	{
		this.ftpPassword = ftpPassword;
	}
	public String getImgFTPStoreBasePath()
	{
		return imgFTPStoreBasePath;
	}
	public void setImgFTPStoreBasePath(String imgFTPStoreBasePath)
	{
		this.imgFTPStoreBasePath = imgFTPStoreBasePath;
	}
	public String getImgFTPVisitPrefx()
	{
		return imgFTPVisitPrefx;
	}
	public void setImgFTPVisitPrefx(String imgFTPVisitPrefx)
	{
		this.imgFTPVisitPrefx = imgFTPVisitPrefx;
	}
	public String getImgLocalVisitPrefix()
	{
		return imgLocalVisitPrefix;
	}
	public void setImgLocalVisitPrefix(String imgLocalVisitPrefix)
	{
		this.imgLocalVisitPrefix = imgLocalVisitPrefix;
	}
	


}
