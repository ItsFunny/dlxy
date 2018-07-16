/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月10日 上午8:11:39
* 
*/
package com.dlxy.system.model;

import java.util.Date;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月10日 上午8:11:39
 */
public class ArticleVO
{
	private Long articleId;
	private Integer titleId;
	private String articleName;
	private String articleAuthor;
	private String articleContent;
	private Integer articleIsRecommend = 0;
	private Integer articleStatus;
	private Date createDate;
	private String titleName;
	private Integer titleParentId = 0;

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

}
