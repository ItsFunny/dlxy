package com.dlxy.model;


import javax.validation.constraints.Max;

import org.hibernate.validator.constraints.NotBlank;

import com.dlxy.common.dto.DlxyTitleDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月8日 上午10:38:26
*/
public class FormTitle
{
	@NotBlank(message="titleName不可为空")
	private String titleName;
	private String titleAbbName;
	
	@Max(value=100,message="最大不能超过100")
	private Integer titleDisplaySeq;
	
	private Integer titleParentId=0;
	
	private Integer titleId;
	
	public void to(DlxyTitleDTO dlxyTitleDTO)
	{
		dlxyTitleDTO.setTitleDisplaySeq(this.titleDisplaySeq);
		dlxyTitleDTO.setTitleName(this.titleName);
		dlxyTitleDTO.setTitleParentId(this.titleParentId);
		dlxyTitleDTO.setTitleId(this.titleId);
		dlxyTitleDTO.setTitleAbbName(this.titleAbbName);
	}
	
	public Integer getTitleDisplaySeq()
	{
		return titleDisplaySeq;
	}
	public void setTitleDisplaySeq(Integer titleDisplaySeq)
	{
		this.titleDisplaySeq = titleDisplaySeq;
	}
	public Integer getTitleParentId()
	{
		return titleParentId;
	}
	public void setTitleParentId(Integer titleParentId)
	{
		this.titleParentId = titleParentId;
	}


	public String getTitleName()
	{
		return titleName;
	}


	public void setTitleName(String titleName)
	{
		this.titleName = titleName;
	}


	public Integer getTitleId()
	{
		return titleId;
	}


	public void setTitleId(Integer titleId)
	{
		this.titleId = titleId;
	}

	public String getTitleAbbName()
	{
		return titleAbbName;
	}

	public void setTitleAbbName(String titleAbbName)
	{
		this.titleAbbName = titleAbbName;
	}
	
}
