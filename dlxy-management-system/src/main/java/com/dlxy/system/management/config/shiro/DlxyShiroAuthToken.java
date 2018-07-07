/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月7日 上午10:20:24
* 
*/
package com.dlxy.system.management.config.shiro;

import java.io.Serializable;

import org.apache.shiro.authc.AuthenticationToken;

import com.dlxy.common.dto.UserDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月7日 上午10:20:24
*/
public class DlxyShiroAuthToken implements AuthenticationToken,Serializable
{

	/**
	* 
	* @Description
	* @author joker 
	* @date 创建时间：2018年7月7日 上午10:26:12
	*/
	private static final long serialVersionUID = -717647863005472200L;
	
	private UserDTO principal;
	private String password;
	
	public DlxyShiroAuthToken(UserDTO userDTO,String password)
	{
		this.principal=userDTO;
		this.password=password;
	}
	@Override
	public Object getPrincipal()
	{
		return principal;
	}

	@Override
	public Object getCredentials()
	{
		return password;
	}

}
