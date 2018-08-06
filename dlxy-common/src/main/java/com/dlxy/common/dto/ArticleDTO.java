/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午2:13:09
* 
*/
package com.dlxy.common.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.joker.library.utils.CommonUtils;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月28日 下午2:13:09
 */
public class ArticleDTO extends AbstractDlxyArticleComposite
{
	private Long articleId;
	private Integer titleId;
	private String articleName;
	private String articleAuthor;
	private String articleContent;
	//2018-07-18 change recommend -> articleType   0:normal article 1 recommend article , 2 picture article 
//	private Integer articleIsRecommend=0;
	private Integer articleType=0;
	private Integer articleStatus;
	private Date createDate;
	private Date updateDate;
	private String createDateStr;
	private String updateDateStr;
	// 2018-06-30 11:30 add
	private Long userId;
//	@JsonIgnore
	private String realname;
	// 2018-06-30 22:02 add
	private Date deleteDate;
	private String deleteDateStr;
	//2018-07-05 17:33 add
	private List<Long> pictureIds;
	//2018-07-06 17:27 add
	private String titleName;
	//2018-07-07 15:02 add
	private Integer titleParentId=0;
	//2018-07-18 19:58 add
	private String pictureUrl;
	//2018-07-22 13:40 add
	private Integer visitCount;
	//2018-08-06 14:04 add
	private Long startTime;
	
	
	
	
	
	
	

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

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		if(null!=createDate)
		{
			this.createDateStr = CommonUtils.simpleDate2String(createDate);
		}
		this.createDate = createDate;
	}

	public Date getUpdateDate()
	{
		return updateDate;
	}

	public void setUpdateDate(Date updateDate)
	{
		if(null!=updateDate)
		{
			this.updateDateStr = CommonUtils.simpleDate2String(updateDate);
		}
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


	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}


	public Date getDeleteDate()
	{
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate)
	{
		this.deleteDate = deleteDate;
		if(null!=deleteDate)
		{
			this.deleteDateStr=CommonUtils.simpleDate2String(deleteDate);
		}
	}

	public String getDeleteDateStr()
	{
		return deleteDateStr;
	}

	public void setDeleteDateStr(String deleteDateStr)
	{
		this.deleteDateStr = deleteDateStr;
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

	public String getRealname()
	{
		return realname;
	}

	public void setRealname(String realname)
	{
		this.realname = realname;
	}

	public Integer getArticleType()
	{
		return articleType;
	}

	public void setArticleType(Integer articleType)
	{
		this.articleType = articleType;
	}

	public String getPictureUrl()
	{
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl)
	{
		this.pictureUrl = pictureUrl;
	}

	public Integer getVisitCount()
	{
		return visitCount;
	}

	public void setVisitCount(Integer visitCount)
	{
		this.visitCount = visitCount;
	}

	public List<Long> getPictureIds()
	{
		return pictureIds;
	}

	public void setPictureIds(List<Long> pictureIds)
	{
		this.pictureIds = pictureIds;
	}

	public Long getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Long startTime)
	{
		this.startTime = startTime;
	}

}
