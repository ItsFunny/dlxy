/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午8:50:20
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
 * @date 创建时间：2018年7月1日 下午8:50:20
 */
public class PictureDTO
{
	private String pictureId;
	private Long articleId;
	private String pictureUrl;
	private Integer pictureType;
	private Integer pictureStatus;
	private Date createDate;
	

	public String getPictureId()
	{
		return pictureId;
	}

	public void setPictureId(String pictureId)
	{
		this.pictureId = pictureId;
	}

	public Long getArticleId()
	{
		return articleId;
	}

	public void setArticleId(Long articleId)
	{
		this.articleId = articleId;
	}

	public Integer getPictureType()
	{
		return pictureType;
	}

	public void setPictureType(Integer pictureType)
	{
		this.pictureType = pictureType;
	}

	public Integer getPictureStatus()
	{
		return pictureStatus;
	}

	public void setPictureStatus(Integer pictureStatus)
	{
		this.pictureStatus = pictureStatus;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public String getPictureUrl()
	{
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl)
	{
		this.pictureUrl = pictureUrl;
	}

}
