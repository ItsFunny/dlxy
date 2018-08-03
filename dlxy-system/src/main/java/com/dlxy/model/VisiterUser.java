package com.dlxy.model;

import java.util.Map;

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
