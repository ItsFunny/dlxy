/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月22日 上午11:52:38
* 
*/
package com.dlxy.model;

import java.util.Map;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月22日 上午11:52:38
*/
public class VisiterUser
{
	private String ip;
	private Map<Long, Long>detail;
	
	public String getIp()
	{
		return ip;
	}
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	public Map<Long, Long> getDetail()
	{
		return detail;
	}
	public void setDetail(Map<Long, Long> detail)
	{
		this.detail = detail;
	}
	
}
