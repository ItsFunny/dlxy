/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月6日 上午8:18:16
* 
*/
package com.dlxy.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.NotBlank;

import com.dlxy.common.dto.UserDTO;
import com.joker.library.utils.CommonUtils;
import com.joker.library.utils.KeyUtils;

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
	@NotBlank(message="姓名不能为空")
	private String realname;
	@NotBlank(message="密码不能为空")
	private String password;
	@Max(value=2,message="权限id最大不能超过2")
	private Integer roleId;
	
	
	public void valid()
	{
		CommonUtils.validStringException(this.realname);
		CommonUtils.validStringException(this.password);
		if(null==roleId)
		{
			throw new RuntimeException("角色id不能为空");
		}
	}
	public void to(UserDTO userDTO)
	{
		userDTO.setRealname(this.realname);
		userDTO.setRoleId(this.roleId);
		userDTO.setPassword(KeyUtils.md5Encrypt(this.password));
		userDTO.setAble(true);
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
