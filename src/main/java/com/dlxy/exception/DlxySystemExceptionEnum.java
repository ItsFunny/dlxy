/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午8:49:09
* 
*/
package com.dlxy.exception;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月29日 下午8:49:09
*/
public enum DlxySystemExceptionEnum
{
	USER_NOT_LOGIN("用户尚未登录");
	private String msg;
	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	private DlxySystemExceptionEnum(String msg)
	{
		this.msg = msg;
	}	
	
	
	
	
}
