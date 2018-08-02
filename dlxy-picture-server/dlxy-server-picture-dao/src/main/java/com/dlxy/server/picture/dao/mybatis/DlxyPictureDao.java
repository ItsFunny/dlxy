package com.dlxy.server.picture.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlxy.server.picture.model.DlxyPicture;
import com.dlxy.server.picture.model.DlxyPictureExample;

public interface DlxyPictureDao {
    int countByExample(DlxyPictureExample example);

    int deleteByExample(DlxyPictureExample example);

    int deleteByPrimaryKey(Long pictureId);

    int insert(DlxyPicture record);

    int insertSelective(DlxyPicture record);

    List<DlxyPicture> selectByExample(DlxyPictureExample example);

    DlxyPicture selectByPrimaryKey(Long pictureId);

    int updateByExampleSelective(@Param("record") DlxyPicture record, @Param("example") DlxyPictureExample example);

    int updateByExample(@Param("record") DlxyPicture record, @Param("example") DlxyPictureExample example);

    int updateByPrimaryKeySelective(DlxyPicture record);

    int updateByPrimaryKey(DlxyPicture record);
}