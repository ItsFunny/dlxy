package com.dlxy.model;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

//import org.hibernate.validator.constraints.NotBlank;

import com.dlxy.common.dto.ArticleDTO;
import com.joker.library.utils.CommonUtils;

public class FormArticle
{
	@NotNull
	private Long articleId;
	@NotNull
	private Integer titleId;
	@NotBlank(message="文章名称不能为空")
	private String articleName;
	@NotBlank(message="文章作者不能为空")
	private String articleAuthor;
	@NotBlank(message="文章内容不能为空")
	private String articleContent;
	// private Integer articleIsRecommend;
	@Max(value=2,message="文章状态最大为2")
	private Integer articleStatus;
	
	@Max(value=5,message="文章类型最大为5")
	private Integer articleType;

	public void to(ArticleDTO articleDTO)
	{
		articleDTO.setArticleAuthor(this.articleAuthor);
		articleDTO.setArticleId(this.articleId);
		articleDTO.setArticleName(this.articleName);
		articleDTO.setArticleStatus(this.articleStatus);
		articleDTO.setArticleContent(this.articleContent);
		articleDTO.setTitleId(this.titleId);
		articleDTO.setArticleType(this.articleType);
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
	public Integer getArticleType()
	{
		return articleType;
	}
	public void setArticleType(Integer articleType)
	{
		this.articleType = articleType;
	}

	// private Date createDate;
	// private Date updateDate;
	// private String createDateStr;
	// private String updateDateStr;
}
