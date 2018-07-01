/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午3:15:52
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
 * @date 创建时间：2018年7月1日 下午3:15:52
 */
public class DlxyTitleDTO implements Serializable
{
	/**
	* 
	* @Description
	* @author joker 
	* @date 创建时间：2018年7月1日 下午4:25:36
	*/
	private static final long serialVersionUID = 3620272655822405536L;
	private Integer titleId;
	private String titleName;
	private Integer titleParentId;
	private Integer titleDisplaySeq;
	private Date createDate;
	private Date updateDate;

	public Integer getTitleId()
	{
		return titleId;
	}

	public void setTitleId(Integer titleId)
	{
		this.titleId = titleId;
	}

	public String getTitleName()
	{
		return titleName;
	}

	public void setTitleName(String titleName)
	{
		this.titleName = titleName;
	}

	public Integer getTitleParentId()
	{
		return titleParentId;
	}

	public void setTitleParentId(Integer titleParentId)
	{
		this.titleParentId = titleParentId;
	}

	public Integer getTitleDisplaySeq()
	{
		return titleDisplaySeq;
	}

	public void setTitleDisplaySeq(Integer titleDisplaySeq)
	{
		this.titleDisplaySeq = titleDisplaySeq;
	}


	public Date getUpdateDate()
	{
		return updateDate;
	}

	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
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
