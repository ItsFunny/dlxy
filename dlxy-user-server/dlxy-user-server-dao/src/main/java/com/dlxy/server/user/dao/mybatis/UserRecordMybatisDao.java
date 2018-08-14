package com.dlxy.server.user.dao.mybatis;


import java.util.Collection;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import com.dlxy.common.dto.UserRecordDTO;

@Mapper
public interface UserRecordMybatisDao
{
	/*
	 * 这个需要改
	 */
	@Insert("insert into dlxy_record (user_id,record_detail,create_date) values (#{userId},#{recordDetail},#{createDate})")
	void addRecord(UserRecordDTO userRecordDTO);
	
	Collection<UserRecordDTO> findRecordsByPage(int start, int end, Map<String, Object> params);
	
	
	
	
}
