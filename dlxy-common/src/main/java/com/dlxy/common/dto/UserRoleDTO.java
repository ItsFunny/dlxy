/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月7日 上午10:50:47
* 
*/
package com.dlxy.common.dto;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月7日 上午10:50:47
 */
public class UserRoleDTO
{
	private Integer roleId;
	private String roleName;
	private String roleDescription;

	public Integer getRoleId()
	{
		return roleId;
	}

	public void setRoleId(Integer roleId)
	{
		this.roleId = roleId;
	}

	public String getRoleName()
	{
		return roleName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	public String getRoleDescription()
	{
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription)
	{
		this.roleDescription = roleDescription;
	}

}
