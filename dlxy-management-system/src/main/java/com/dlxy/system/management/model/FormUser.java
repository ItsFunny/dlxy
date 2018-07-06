/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月6日 上午8:18:16
* 
*/
package com.dlxy.system.management.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import com.dlxy.common.dto.UserDTO;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月6日 上午8:18:16
 */
public class FormUser
{
	@NotNull
	private String realname;
	@NotNull
	private String password;
	@Max(2)
	private Integer roleId;
	
	public void to(UserDTO userDTO)
	{
		userDTO.setRealname(this.realname);
		userDTO.setUsername(this.realname);
		userDTO.setRoleId(this.roleId);
		userDTO.setPassword(this.password);
	}
	public String getRealname()
	{
		return realname;
	}

	public void setRealname(String realname)
	{
		this.realname = realname;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Integer getRoleId()
	{
		return roleId;
	}

	public void setRoleId(Integer roleId)
	{
		this.roleId = roleId;
	}

}
