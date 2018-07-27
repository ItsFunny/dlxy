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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.server.user.dao.mybatis.DlxyUserDao;
import com.dlxy.server.user.dao.mybatis.UserMybatisDao;
import com.dlxy.server.user.dao.mybatis.UserRecordMybatisDao;
import com.dlxy.server.user.dao.query.UserArticleQueryDao;
import com.dlxy.server.user.dao.query.UserQueryDao;
import com.dlxy.server.user.dao.query.UserRecordQueryDao;
import com.dlxy.server.user.model.DlxyUser;
import com.dlxy.server.user.model.DlxyUserExample;
import com.dlxy.server.user.model.DlxyUserExample.Criteria;
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
		DlxyUserExample example = new DlxyUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andRealnameEqualTo(username);
		List<UserDTO> users = userMybatisDao.selectByExample(example);
		if(null==users || users.isEmpty())
		{
			return null;
		}
		return users.iterator().next();
	}

	@Override
	public UserDTO findByUserId(Long userId) throws SQLException
	{
//		return userMybatisDao.findByUserId(userId);
		return userMybatisDao.selectByPrimaryKey(userId);
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
//		userMybatisDao.addUser(userDTO);
		userMybatisDao.insertSelective(userDTO);
		return userDTO.getUserId();
	}

	@Override
	public void updateUserStatusByUserId(Long userId, Integer status)
	{
		userMybatisDao.updateUserStatusByUserId(userId, status);
	}

	@Override
	public int updateUserByUserId(DlxyUser user)
	{
//		UserDTO dbUser = findByUserId(user.getUserId());
//		if(StringUtils.isBlank(user.getRealname()) || user.getRealname().equals(dbUser.getRealname()))
//		{
//			
//		}
		return userMybatisDao.updateByPrimaryKeySelective(user);
	}

	@Override
	public void updateUserByExample(DlxyUser user)
	{
		DlxyUserExample example=new DlxyUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(user.getUserId());
		userMybatisDao.updateByPrimaryKeySelective(user);
	}

	@Override
	public Integer deleteUser(List<Long> userIds)
	{
		DlxyUserExample example=new DlxyUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdIn(userIds);
		int i=userMybatisDao.deleteByExample(example);
		return i;
	}

	@Override
	public Integer deleteUseByUserId(Long userId)
	{
		return userMybatisDao.deleteByPrimaryKey(userId);
	}

}
