/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月8日 上午12:05:51
* 
*/
package com.dlxy.system.management.exception;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月8日 上午12:05:51
*/
public class ManagementIllegalException extends RuntimeException
{

	/**
	* 
	* @Description
	* @author joker 
	* @date 创建时间：2018年7月8日 上午12:08:42
	*/
	private static final long serialVersionUID = -758841428074965716L;
	
	private String ip;
	
	
	public ManagementIllegalException(String ip, Long userId, String detail)
	{
		super();
		this.ip = ip;
		this.userId = userId;
		this.detail = detail;
	}

	private Long userId;
	
	private String detail;

	
	
	public ManagementIllegalException()
	{
		super();
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getDetail()
	{
		return detail;
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}
	
	
	

}
