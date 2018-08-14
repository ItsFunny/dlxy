package com.dlxy.server.user.model;

public class DlxyVisitRecord {
    private Long visitId;

    private Integer visitRecordType;

    private Integer visitCount;

    private Long visitRecordDate;

    public Long getVisitId() {
        return visitId;
    }

    public void setVisitId(Long visitId) {
        this.visitId = visitId;
    }

    public Integer getVisitRecordType() {
        return visitRecordType;
    }

    public void setVisitRecordType(Integer visitRecordType) {
        this.visitRecordType = visitRecordType;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public Long getVisitRecordDate() {
        return visitRecordDate;
    }

    public void setVisitRecordDate(Long visitRecordDate) {
        this.visitRecordDate = visitRecordDate;
    }
}