/**
*
* @author joker 
* @date 创建时间：2018年6月7日 下午12:45:50
* 
*/
package com.dlxy.manager.config.property;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
	@Value("${dlxy.key.property-private-key-path}")
	private String propertyPrivateKeyPath;
	
	@Value("${dlxy.key.property-public-key-path}")
	private String propertyPublicKeyPath;
	
	private byte[] propertyPublicKey;
	
	private byte[] propertyPrivateKey;
	
	private void init()
	{
		PathMatchingResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource(propertyPrivateKeyPath);
		try
		{
			InputStream inputStream = resource.getInputStream();
			int i=0;
			StringBuilder sb=new StringBuilder();
			while((i=inputStream.read())!=-1)
			{
					sb.append((char)i);
			}
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void afterPropertiesSet() throws Exception
	{
		// TODO Auto-generated method stub
		
	}

}
