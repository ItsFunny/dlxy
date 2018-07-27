package com.dlxy.server.picture.model;

import java.util.Date;

public class DlxyPicture {
    private Long pictureId;

    private String pictureUrl;

    private Integer pictureDisplaySeq;

    private Date createDate;

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl == null ? null : pictureUrl.trim();
    }

    public Integer getPictureDisplaySeq() {
        return pictureDisplaySeq;
    }

    public void setPictureDisplaySeq(Integer pictureDisplaySeq) {
        this.pictureDisplaySeq = pictureDisplaySeq;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}