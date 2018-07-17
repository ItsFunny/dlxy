/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 上午9:02:41
* 
*/
package com.dlxy.common.dto;

import java.util.Date;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月29日 上午9:02:41
 */
public class UserDTO
{
	private Long userId;
//	private String username;
	private String realname;
	private String password;
	private Integer roleId;
	private String lastLoginIp;
	private Date lastLoginDate;
	private Date createDate;
	private Date updateDate;
	 
	//2018-07-17 add
	private boolean able;  //1 able 0 disable
	//2018-07-18 00:06 add
	private String roleDescription;
	private String roleName;
	
	
	

	public boolean isAdmin()
	{
		return this.roleId==0;
	}
	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

//	public String getUsername()
//	{
//		return username;
//	}
//
//	public void setUsername(String username)
//	{
//		this.username = username;
//	}

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

	public String getLastLoginIp()
	{
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp)
	{
		this.lastLoginIp = lastLoginIp;
	}

	public Date getLastLoginDate()
	{
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate)
	{
		this.lastLoginDate = lastLoginDate;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public Date getUpdateDate()
	{
		return updateDate;
	}

	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}
	public boolean isAble()
	{
		return able;
	}
	public void setAble(boolean able)
	{
		this.able = able;
	}
	public String getRoleDescription()
	{
		return roleDescription;
	}
	public void setRoleDescription(String roleDescription)
	{
		this.roleDescription = roleDescription;
	}
	public String getRoleName()
	{
		return roleName;
	}
	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

}
