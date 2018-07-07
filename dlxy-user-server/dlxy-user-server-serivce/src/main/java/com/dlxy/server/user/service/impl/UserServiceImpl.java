/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月4日 下午5:41:39
* 
*/
package com.dlxy.server.user.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.server.user.dao.mybatis.UserMybatisDao;
import com.dlxy.server.user.dao.mybatis.UserRecordMybatisDao;
import com.dlxy.server.user.dao.query.UserArticleQueryDao;
import com.dlxy.server.user.dao.query.UserQueryDao;
import com.dlxy.server.user.dao.query.UserRecordQueryDao;
import com.dlxy.server.user.service.IUserArticleService;
import com.dlxy.server.user.service.IUserRecordService;
import com.dlxy.server.user.service.IUserService;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月4日 下午5:41:39
 */
// 感觉是强聚合的,所以直接合并到同个实现类中,避免内存中类太多太杂
@Service
public class UserServiceImpl implements IUserArticleService, IUserService, IUserRecordService
{
	@Autowired
	private UserRecordMybatisDao userRecordMybatisDao;

	@Autowired
	private UserRecordQueryDao userRecordQueryDao;
	@Autowired
	private UserQueryDao userQueryDao;
	@Autowired
	private UserMybatisDao userMybatisDao;

	@Autowired
	private UserArticleQueryDao userArticleQueryDao;

	public void addRecord(UserRecordDTO userRecordDTO)
	{
		userRecordMybatisDao.addRecord(userRecordDTO);
	}

	@Override
	public Collection<Map<String, Object>> findByPage(int pageSize, int pageNum, Map<String, Object> params)
			throws SQLException
	{
		if (pageNum <= 0)
		{
			pageNum = 1;
		}
		if (pageSize <= 0)
		{
			pageSize = 10;
		}
		params.put("start", (pageNum - 1) * pageSize);
		params.put("end", pageSize);
		return userArticleQueryDao.findByPage(params);
	}

	@Override
	public Long countByParam(Map<String, Object> params) throws SQLException
	{
		if (params.containsKey("searchParam"))
		{
			String q = params.get("searchParam").toString();
			try
			{
				long userId = Long.parseLong(q);
				params.put("userId", userId);
			} catch (NumberFormatException e)
			{
				params.put("username",q);
			}
		}
		return userArticleQueryDao.countByParam(params);
	}

	@Override
	public UserDTO findUserByNameOrId(String key) throws SQLException
	{
		Long userId = null;
		try
		{
			userId = Long.parseLong(key);
			return findByUserId(userId);
		} catch (Exception e)
		{
			return findByUsername(key);
		}
	}

	@Override
	public UserDTO findByUsername(String username)
	{
		return userMybatisDao.findByUsername(username);
	}

	@Override
	public UserDTO findByUserId(Long userId) throws SQLException
	{
		return userMybatisDao.findByUserId(userId);
	}

	@Override
	public Collection<UserRecordDTO> findRecordByPage(int pageSize, int pageNum, Map<String, Object> params)
			throws SQLException
	{
		if (pageNum <= 0)
		{
			pageNum = 1;
		}
		if (pageSize <= 0)
		{
			pageSize = 10;
		}
		return userRecordQueryDao.findRecordsByPage((pageNum - 1) * pageSize, pageSize, params);
	}

	@Override
	public Long countRecords(Map<String, Object> params) throws SQLException
	{
		return userRecordQueryDao.coutRecords(params);
	}

	@Override
	public Collection<UserDTO> findUsersByPage(int start, int end, Map<String, Object> params) throws SQLException
	{
		return userMybatisDao.findUsersByPage(start, end, params);
	}

	@Override
	public Long countUsersByParam(Map<String, Object> params) throws SQLException
	{
		return userQueryDao.countUserByParam(params);
	}

	@Override
	public Long addUser(UserDTO userDTO) throws SQLException
	{
		userDTO.setCreateDate(new Date());
		userMybatisDao.addUser(userDTO);
		return userDTO.getUserId();
	}

}
