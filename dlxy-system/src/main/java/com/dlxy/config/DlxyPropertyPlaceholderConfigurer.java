/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月31日 上午7:25:53
* 
*/
package com.dlxy.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月31日 上午7:25:53
*/
public class DlxyPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer
{

	@Override
	protected String convertProperty(String propertyName, String propertyValue)
	{
		System.out.println("1");
		return super.convertProperty(propertyName, propertyValue);
	}
	

}
