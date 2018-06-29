/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午4:24:30
* 
*/
package com.dlxy.common.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月29日 下午4:24:30
 */
public class UserRecordDTO implements Serializable
{
	/**
	* 
	* @Description
	* @author joker 
	* @date 创建时间：2018年6月29日 下午4:25:35
	*/
	private static final long serialVersionUID = 5911988410360555531L;
	private Long recordId;
	private Long userId;
	private String recordDetail;
	private Date createDate;

	public Long getRecordId()
	{
		return recordId;
	}

	public void setRecordId(Long recordId)
	{
		this.recordId = recordId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getRecordDetail()
	{
		return recordDetail;
	}

	public void setRecordDetail(String recordDetail)
	{
		this.recordDetail = recordDetail;
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