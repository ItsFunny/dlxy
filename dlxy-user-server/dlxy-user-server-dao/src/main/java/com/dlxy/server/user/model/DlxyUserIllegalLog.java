package com.dlxy.server.user.model;

import java.util.Date;

public class DlxyUserIllegalLog {
    private Long illegalLogId;

    private Long userId;

    private String ip;

    private String illegalDetail;

    private Boolean illegalLevel;

    private Date createDate;

    private Date updateDate;

    public Long getIllegalLogId() {
        return illegalLogId;
    }

    public void setIllegalLogId(Long illegalLogId) {
        this.illegalLogId = illegalLogId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getIllegalDetail() {
        return illegalDetail;
    }

    public void setIllegalDetail(String illegalDetail) {
        this.illegalDetail = illegalDetail == null ? null : illegalDetail.trim();
    }

    public Boolean getIllegalLevel() {
        return illegalLevel;
    }

    public void setIllegalLevel(Boolean illegalLevel) {
        this.illegalLevel = illegalLevel;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}