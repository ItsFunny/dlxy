package com.dlxy.server.user.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlxy.common.dto.UserDTO;
import com.dlxy.server.user.model.DlxyUser;
import com.dlxy.server.user.model.DlxyUserExample;

@Mapper
public interface DlxyUserDao {
    int countByExample(DlxyUserExample example);

    int deleteByExample(DlxyUserExample example);

    int deleteByPrimaryKey(Long userId);

    int insert(UserDTO record);

    int insertSelective(UserDTO record);

    List<UserDTO> selectByExample(DlxyUserExample example);

    UserDTO selectByPrimaryKey(Long userId);

    int updateByExampleSelective(@Param("record") DlxyUser record, @Param("example") DlxyUserExample example);

    int updateByExample(@Param("record") DlxyUser record, @Param("example") DlxyUserExample example);

    //1
    int updateByPrimaryKeySelective(DlxyUser record);

    int updateByPrimaryKey(UserDTO record);
}