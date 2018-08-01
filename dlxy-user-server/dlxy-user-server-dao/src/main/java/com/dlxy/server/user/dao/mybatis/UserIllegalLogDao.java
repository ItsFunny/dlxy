package com.dlxy.server.user.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlxy.server.user.model.DlxyUserIllegalLog;
import com.dlxy.server.user.model.DlxyUserIllegalLogExample;


@Mapper
public interface UserIllegalLogDao {
    long countByExample(DlxyUserIllegalLogExample example);

    int deleteByExample(DlxyUserIllegalLogExample example);

    int deleteByPrimaryKey(Long illegalLogId);

    int insert(DlxyUserIllegalLog record);

    int insertSelective(DlxyUserIllegalLog record);

    List<DlxyUserIllegalLog> selectByExample(DlxyUserIllegalLogExample example);

    DlxyUserIllegalLog selectByPrimaryKey(Long illegalLogId);

    int updateByExampleSelective(@Param("record") DlxyUserIllegalLog record, @Param("example") DlxyUserIllegalLogExample example);

    int updateByExample(@Param("record") DlxyUserIllegalLog record, @Param("example") DlxyUserIllegalLogExample example);

    int updateByPrimaryKeySelective(DlxyUserIllegalLog record);

    int updateByPrimaryKey(DlxyUserIllegalLog record);
}