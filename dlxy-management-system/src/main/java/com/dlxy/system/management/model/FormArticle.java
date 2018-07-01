/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午10:54:55
* 
*/
package com.dlxy.system.management.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.springframework.lang.NonNull;

import com.dlxy.common.dto.ArticleDTO;
import com.joker.library.utils.CommonUtils;
import com.sun.tools.javac.resources.compiler;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月1日 下午10:54:55
 */
public class FormArticle
{
	@NonNull
	private Long articleId;
	@NonNull
	private Integer titleId;
	@NotNull
	private String articleName;
	@NotNull
	private String articleAuthor;
	@NotNull
	private String articleContent;
	// private Integer articleIsRecommend;
	@Max(2)
	private Integer articleStatus;

	public void to(ArticleDTO articleDTO)
	{
		articleDTO.setArticleAuthor(this.articleAuthor);
		articleDTO.setArticleId(this.articleId);
		articleDTO.setArticleName(this.articleName);
		articleDTO.setArticleStatus(this.articleStatus);
		articleDTO.setArticleContent(this.articleContent);
		articleDTO.setTitleId(this.titleId);
	}
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

	public Integer getArticleStatus()
	{
		return articleStatus;
	}

	public void setArticleStatus(Integer articleStatus)
	{
		this.articleStatus = articleStatus;
	}

	public void isInvalid()
	{
		CommonUtils.validStringException(this.articleAuthor);
		CommonUtils.validStringException(this.articleName);
	}

	// private Date createDate;
	// private Date updateDate;
	// private String createDateStr;
	// private String updateDateStr;
}
