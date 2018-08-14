package com.dlxy.server.user.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dlxy.server.user.model.DlxyLink;
import com.dlxy.server.user.model.DlxyLinkExample;

public interface DlxyLinkDao {
    long countByExample(DlxyLinkExample example);

    int deleteByExample(DlxyLinkExample example);

    int deleteByPrimaryKey(Integer linkId);

    int insert(DlxyLink record);

    int insertSelective(DlxyLink record);

    List<DlxyLink> selectByExample(DlxyLinkExample example);

    DlxyLink selectByPrimaryKey(Integer linkId);

    int updateByExampleSelective(@Param("record") DlxyLink record, @Param("example") DlxyLinkExample example);

    int updateByExample(@Param("record") DlxyLink record, @Param("example") DlxyLinkExample example);

    int updateByPrimaryKeySelective(DlxyLink record);

    int updateByPrimaryKey(DlxyLink record);
}