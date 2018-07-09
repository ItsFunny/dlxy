/**
*
* @author joker 
* @date 创建时间：2018年6月7日 下午12:32:50
* 
*/
package com.dlxy.system.config;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


/**
* 
* @author joker 
* @date 创建时间：2018年6月7日 下午12:32:50
*/
public class DlxyPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer
{

	@Override
	protected void convertProperties(Properties arg0)
	{
		super.convertProperties(arg0);
	}

	@Override
	protected String convertProperty(String propertyName, String propertyValue)
	{
		System.out.println("call convertProperty");
		if(propertyName.equals("dlxy.db.password"))
		{
			System.out.println("the properety-name is password");
		}
			
		return propertyValue;
	}

	
}
