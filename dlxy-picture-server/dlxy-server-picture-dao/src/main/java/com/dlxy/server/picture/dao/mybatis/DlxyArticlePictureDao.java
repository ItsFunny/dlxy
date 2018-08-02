package com.dlxy.server.picture.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlxy.server.picture.model.DlxyArticlePicture;
import com.dlxy.server.picture.model.DlxyArticlePictureExample;


public interface DlxyArticlePictureDao {
    int countByExample(DlxyArticlePictureExample example);

    int deleteByExample(DlxyArticlePictureExample example);

    int insert(DlxyArticlePicture record);

    int insertSelective(DlxyArticlePicture record);

    List<DlxyArticlePicture> selectByExample(DlxyArticlePictureExample example);

    int updateByExampleSelective(@Param("record") DlxyArticlePicture record, @Param("example") DlxyArticlePictureExample example);

    int updateByExample(@Param("record") DlxyArticlePicture record, @Param("example") DlxyArticlePictureExample example);
}