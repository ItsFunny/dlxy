/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月9日 下午8:32:17
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
 * @date 创建时间：2018年7月9日 下午8:32:17
 */
public class IllegalLogDTO
{
	private String ip;
	private Long userId;
	private String illegalDetail;
	private Integer illegalLevel;
	private Date createDate;
	
	
	public IllegalLogDTO(String ip,Long userid,String detail,Integer level)
	{
		this.ip=ip;
		this.userId=userid;
		this.illegalDetail=detail;
		this.illegalLevel=level;
	}
	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getIllegalDetail()
	{
		return illegalDetail;
	}

	public void setIllegalDetail(String illegalDetail)
	{
		this.illegalDetail = illegalDetail;
	}

	public Integer getIllegalLevel()
	{
		return illegalLevel;
	}

	public void setIllegalLevel(Integer illegalLevel)
	{
		this.illegalLevel = illegalLevel;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

}
