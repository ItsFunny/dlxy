/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月20日 下午4:49:07
* 
*/
package com.dlxy.common.dto;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月20日 下午4:49:07
 */
public class SuspicionDTO
{
	private String ip;
	private Long visitTime;
	private Integer level;
	private String url;
	public SuspicionDTO(String ip,Integer level,String url)
	{
		this.ip=ip;
		this.level=level;
		this.url=url;
	}
	public SuspicionDTO()
	{
		
	}
	
	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public Long getVisitTime()
	{
		return visitTime;
	}

	public void setVisitTime(Long visitTime)
	{
		this.visitTime = visitTime;
	}

	public Integer getLevel()
	{
		return level;
	}

	public void setLevel(Integer level)
	{
		this.level = level;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}


}
