/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午2:13:09
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
 * @date 创建时间：2018年6月28日 下午2:13:09
 */
public class ArticleDTO
{
	private Long articleId;
	private Integer titleId;
	private String articleName;
	private String articleAuthor;
	private String articleContent;
	private Integer articleIsRecommend;
	private Integer articleStatus;
	private Date createDate;
	private Date updateDate;
	private String createDateStr;
	private String updateDateStr;

	public Long getArticleId()
	{
		return articleId;
	}

	public void setArticleId(Long articleId)
	{
		this.articleId = articleId;
	}

	public Integer getTitleId()
	{
		return titleId;
	}

	public void setTitleId(Integer titleId)
	{
		this.titleId = titleId;
	}

	public String getArticleName()
	{
		return articleName;
	}

	public void setArticleName(String articleName)
	{
		this.articleName = articleName;
	}

	public String getArticleAuthor()
	{
		return articleAuthor;
	}

	public void setArticleAuthor(String articleAuthor)
	{
		this.articleAuthor = articleAuthor;
	}

	public String getArticleContent()
	{
		return articleContent;
	}

	public void setArticleContent(String articleContent)
	{
		this.articleContent = articleContent;
	}

	public Integer getArticleIsRecommend()
	{
		return articleIsRecommend;
	}

	public void setArticleIsRecommend(Integer articleIsRecommend)
	{
		this.articleIsRecommend = articleIsRecommend;
	}

	public Integer getArticleStatus()
	{
		return articleStatus;
	}

	public void setArticleStatus(Integer articleStatus)
	{
		this.articleStatus = articleStatus;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public Date getUpdateDate()
	{
		return updateDate;
	}

	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}

	public String getCreateDateStr()
	{
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr)
	{
		this.createDateStr = createDateStr;
	}

	public String getUpdateDateStr()
	{
		return updateDateStr;
	}

	public void setUpdateDateStr(String updateDateStr)
	{
		this.updateDateStr = updateDateStr;
	}

	@Override
	public String toString()
	{
		return "ArticleDTO [articleId=" + articleId + ", titleId=" + titleId + ", articleName=" + articleName
				+ ", articleAuthor=" + articleAuthor + ", articleContent=" + articleContent + ", articleIsRecommend="
				+ articleIsRecommend + ", articleStatus=" + articleStatus + ", createDate=" + createDate
				+ ", updateDate=" + updateDate + ", createDateStr=" + createDateStr + ", updateDateStr=" + updateDateStr
				+ "]";
	}

}