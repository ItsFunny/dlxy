/**
*
* @author joker 
* @date 创建时间：2018年6月7日 下午12:50:25
* 
*/
package com.dlxy.manager.config.property;

import org.springframework.beans.factory.annotation.Value;

/**
 * 
 * @author joker
 * @date 创建时间：2018年6月7日 下午12:50:25
 */
public class DlxyProperty
{
	@Value("${dlxy.db.username}")
	private String username;
	@Value("${dlxy.db.password}")
	private String password;
	private String driverClassName = "com.mysql.jdbc.Driver";
	@Value("${dlxy.db.url}")
	private String url;

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

}
