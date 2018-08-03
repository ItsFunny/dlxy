package com.dlxy.server.article.dao.mybatis;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.server.article.model.DlxyTitleExample;

public interface DlxyTitleDao {
    long countByExample(DlxyTitleExample example);

    int deleteByExample(DlxyTitleExample example);

    int deleteByPrimaryKey(Integer titleId);

    int insert(DlxyTitleDTO record);

    int insertSelective(DlxyTitleDTO record);

    List<DlxyTitleDTO> selectByExample(DlxyTitleExample example);

    DlxyTitleDTO selectByPrimaryKey(Integer titleId);

    int updateByExampleSelective(@Param("record") DlxyTitleDTO record, @Param("example") DlxyTitleExample example);

    int updateByExample(@Param("record") DlxyTitleDTO record, @Param("example") DlxyTitleExample example);

    int updateByPrimaryKeySelective(DlxyTitleDTO record);

    int updateByPrimaryKey(DlxyTitleDTO record);
}