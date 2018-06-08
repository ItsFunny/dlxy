/**
*
* @author joker 
* @date 创建时间：2018年6月7日 下午12:45:50
* 
*/
package com.dlxy.system.management.config.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * 
 * @author joker
 * @date 创建时间：2018年6月7日 下午12:45:50
 */
@Component
public class DlxyKeyProperty implements InitializingBean
{
	private Logger logger=LoggerFactory.getLogger(DlxyKeyProperty.class);
	@Value("${dlxy.key.property-private-key-path}")
	private String propertyPrivateKeyPath;

	@Value("${dlxy.key.property-public-key-path}")
	private String propertyPublicKeyPath;

	private byte[] propertyPublicKey;

	private byte[] propertyPrivateKey;

	private void initPropertyPrivateKey()
	{
		initKey(this.propertyPrivateKeyPath, this.propertyPrivateKey);
	}
	private void initPropertyPublicKey()
	{
		initKey(this.propertyPublicKeyPath, this.propertyPublicKey);
	}
	
	private void initKey(String path,byte[] result)
	{
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource(path);
		InputStream inputStream = null;
		try
		{
			logger.info("begin decode the key from {}",path);
			inputStream = resource.getInputStream();
			int i = 0;
			StringBuilder sb = new StringBuilder();
			while ((i = inputStream.read()) != -1)
			{
				sb.append((char) i);
			}
			result = Base64.getDecoder().decode(sb.toString());
		} catch (IOException e)
		{
			logger.error("  decode key fail from {}",path);
			e.printStackTrace();
		} finally
		{
			try
			{
				inputStream.close();
			} catch (IOException e)
			{
				logger.error("close inputstream fail");
				e.printStackTrace();
			}
		}
	}

	

	public void afterPropertiesSet() throws Exception
	{
		initPropertyPrivateKey();
		initPropertyPublicKey();
	}

	public String getPropertyPrivateKeyPath()
	{
		return propertyPrivateKeyPath;
	}

	public void setPropertyPrivateKeyPath(String propertyPrivateKeyPath)
	{
		this.propertyPrivateKeyPath = propertyPrivateKeyPath;
	}

	public String getPropertyPublicKeyPath()
	{
		return propertyPublicKeyPath;
	}

	public void setPropertyPublicKeyPath(String propertyPublicKeyPath)
	{
		this.propertyPublicKeyPath = propertyPublicKeyPath;
	}

	public byte[] getPropertyPublicKey()
	{
		return propertyPublicKey;
	}

	public void setPropertyPublicKey(byte[] propertyPublicKey)
	{
		this.propertyPublicKey = propertyPublicKey;
	}

	public byte[] getPropertyPrivateKey()
	{
		return propertyPrivateKey;
	}

	public void setPropertyPrivateKey(byte[] propertyPrivateKey)
	{
		this.propertyPrivateKey = propertyPrivateKey;
	}

}
