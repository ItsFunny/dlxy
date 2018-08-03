package com.dlxy.shiro;

import java.io.Serializable;

import org.apache.shiro.authc.AuthenticationToken;

import com.dlxy.common.dto.UserDTO;
public class DlxyShiroAuthToken implements AuthenticationToken,Serializable
{

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
