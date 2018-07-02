/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 上午9:02:41
* 
*/
package com.dlxy.common.dto;

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
	private String username;
	
	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}
	
}
