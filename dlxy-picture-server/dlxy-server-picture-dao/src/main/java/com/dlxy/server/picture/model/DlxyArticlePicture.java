package com.dlxy.server.picture.model;

import java.util.Date;

public class DlxyArticlePicture {
    private Long articleId;

    private Long pictureId;

    private Integer pictureStatus;

    private Integer pictureType;

    private Date createDate;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }


    public Integer getPictureType() {
        return pictureType;
    }

    public void setPictureType(Integer pictureType) {
        this.pictureType = pictureType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	public Integer getPictureStatus()
	{
		return pictureStatus;
	}

	public void setPictureStatus(Integer pictureStatus)
	{
		this.pictureStatus = pictureStatus;
	}
}