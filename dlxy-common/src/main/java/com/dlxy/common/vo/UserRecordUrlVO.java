package com.dlxy.common.vo;

public class UserRecordUrlVO
{
	private String id;
	private String url;
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public UserRecordUrlVO(String id, String url)
	{
		super();
		this.id = id;
		this.url = url;
	}
	
}
