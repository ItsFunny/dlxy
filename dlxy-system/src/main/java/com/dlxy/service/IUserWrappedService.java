package com.dlxy.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.server.user.model.DlxyUser;

public interface IUserWrappedService
{
	PageDTO<Collection<Map<String, Object>>>findUserArticlesByPage(int pageSize,int pageNum,Map<String, Object>p) throws SQLException;
	
	PageDTO<Collection<UserRecordDTO>>findUserRecords(int pageSize,int pageNum,Map<String, Object>params) throws SQLException;
	
	PageDTO<Collection<UserDTO>> findUsersByPage(int pageSize,int pageNum,Map<String, Object>params) throws SQLException;
	
	void addUser(UserDTO loginUser,UserDTO userDTO) throws SQLException;
	
	String updateUser(UserDTO loginUser,DlxyUser objUser);
	
	Integer deleteUser(UserDTO loginUser,List<Long> userIds);
	
	Integer deleteUserSingle(UserDTO loginUser,Long userId);
	
	void updateUserStatusByUserId(UserDTO loginUser,Long userId,int status);
	
}
