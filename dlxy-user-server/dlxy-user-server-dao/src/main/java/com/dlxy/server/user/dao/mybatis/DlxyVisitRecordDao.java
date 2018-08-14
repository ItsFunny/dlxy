package com.dlxy.server.user.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dlxy.server.user.model.DlxyVisitRecord;
import com.dlxy.server.user.model.DlxyVisitRecordExample;

public interface DlxyVisitRecordDao {
    long countByExample(DlxyVisitRecordExample example);

    int deleteByExample(DlxyVisitRecordExample example);

    int deleteByPrimaryKey(Long visitId);

    int insert(DlxyVisitRecord record);

    int insertSelective(DlxyVisitRecord record);

    List<DlxyVisitRecord> selectByExample(DlxyVisitRecordExample example);

    DlxyVisitRecord selectByPrimaryKey(Long visitId);

    int updateByExampleSelective(@Param("record") DlxyVisitRecord record, @Param("example") DlxyVisitRecordExample example);

    int updateByExample(@Param("record") DlxyVisitRecord record, @Param("example") DlxyVisitRecordExample example);

    int updateByPrimaryKeySelective(DlxyVisitRecord record);

    int updateByPrimaryKey(DlxyVisitRecord record);
}