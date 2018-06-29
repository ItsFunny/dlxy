/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午7:22:07
* 
*/
package com.dlxy.common.event;

import java.io.Serializable;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月28日 下午7:22:07
 */
public class AppEvent implements Serializable
{
	/**
	* 
	* @Description
	* @author joker 
	* @date 创建时间：2018年6月29日 下午6:22:05
	*/
	private static final long serialVersionUID = 8462581555918864033L;
	private Long startTimeStamp;
	private Serializable data;
	/*
	 * routingKey
	 */
	private String eventType;
	
	
	
	public AppEvent()
	{
		super();
	}

	public AppEvent(Serializable data, String eventType)
	{
		super();
		this.data = data;
		this.eventType = eventType;
	}

	public Long getStartTimeStamp()
	{
		return startTimeStamp;
	}

	public void setStartTimeStamp(Long startTimeStamp)
	{
		this.startTimeStamp = startTimeStamp;
	}

	public Serializable getData()
	{
		return data;
	}

	public void setData(Serializable data)
	{
		this.data = data;
	}

	public String getEventType()
	{
		return eventType;
	}

	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}

}
