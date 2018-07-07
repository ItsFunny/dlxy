/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月3日 下午1:30:17
* 
*/
package com.dlxy.server.user.dao.query.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dlxy.common.dto.UserDTO;
import com.dlxy.server.user.dao.query.UserQueryDao;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月3日 下午1:30:17
 */
@Repository
public class UserQueryDaoImpl implements UserQueryDao
{
	@Autowired
	private QueryRunner queryRunner;

	public static class UserResultSetHandler implements ResultSetHandler<Collection<UserDTO>>
	{
		boolean needPwd;

		public UserResultSetHandler()
		{
			this(false);
		}

		public UserResultSetHandler(boolean i)
		{
			needPwd = i;
		}

		@Override
		public Collection<UserDTO> handle(ResultSet rs) throws SQLException
		{
			// select
			// user_id,username,realname,password,role_id,last_login_ip,last_login_date,create_date,
			// update_date
			List<UserDTO> userDTOs = new ArrayList<UserDTO>();
			while (rs.next())
			{
				UserDTO userDTO = new UserDTO();
				userDTO.setUserId(rs.getLong(1));
				userDTO.setUsername(rs.getString(2));
				userDTO.setRealname(rs.getString(3));
				if (needPwd)
				{
					userDTO.setPassword(rs.getString(4));
				}
				userDTO.setRoleId(rs.getInt(5));
				userDTO.setLastLoginIp(rs.getString(6));
				userDTO.setLastLoginDate(rs.getDate(7));
				userDTO.setCreateDate(rs.getDate(8));
				userDTO.setUpdateDate(rs.getDate(9));
				userDTOs.add(userDTO);
			}
			return userDTOs;
		}

	}

	@Override
	public UserDTO findByNameOrId(String key) throws SQLException
	{
		String sql = "select a.user_id,a.username,a.realname,a.passwrod,a.role_id,a.last_login_ip,a.crete_date,a.update_date from dlxy_user a where 1=1 ";
		List<Object> l = new LinkedList<>();
		try
		{
			Long userId = Long.parseLong(key);
			sql += " and a.user_id = ? ";
			l.add(userId);
		} catch (NumberFormatException e)
		{
			sql += " and a.username = ? ";
//			l.add("%" + key + "%");
			l.add(key);
		}
		Collection<UserDTO> query = queryRunner.query(sql, new UserResultSetHandler(true), l.toArray());
		if (null == query)
		{
			return null;
		} else if (query.size() == 1)
		{
			return query.iterator().next();
		} else
		{
			throw new RuntimeException("find multipart users ");
		}
	}

//	@Override
//	public UserDTO findByUserId(Long userId) throws SQLException
//	{
//		String sql = "select a.user_id,a.username,a.realname,a.passwrod,a.role_id,a.last_login_ip,a.crete_date,a.update_date from dlxy_user a where 1=1 and a.user_id=? ";
//		Collection<UserDTO> query = queryRunner.query(sql, new UserResultSetHandler(true), userId);
//		if (null == query)
//		{
//			return null;
//		} else if (query.size() == 1)
//		{
//			return query.iterator().next();
//		} else
//		{
//			throw new RuntimeException("find multipart users");
//		}
//	}

	@Override
	public Long countUserByParam(Map<String, Object> params) throws SQLException
	{
		String sql = "select count(1) from dlxy_user where 1=1 ";

		Object count = queryRunner.query(sql, new ScalarHandler<Object>());
		if (null == count)
		{
			return 0l;
		} else
		{
			return ((Number) count).longValue();
		}
	}

//	@Override
//	public Collection<UserDTO> findUsersByPage(int start, int end, Map<String, Object> params) throws SQLException
//	{
//		List<Object> l = new LinkedList<>();
//		String sql = "select user_id,username,realname,password,role_id,last_login_ip,last_login_date,create_date, update_date from dlxy_user where 1=1 ";
//
//		sql += "order by create_date desc limit ?,?";
//		l.add(start);
//		l.add(end);
//
//		Collection<UserDTO> query = queryRunner.query(sql, new UserResultSetHandler(), l.toArray());
//
//		return query;
//	}

//	@Override
//	public void addUser(UserDTO userDTO) throws SQLException
//	{
//		String sql = "insert into dlxy_user (username,realname,password,role_id,last_login_ip,last_login_date,create_date,update_date values (?,?,?,?,?,?,?,?)";
//		queryRunner.update(sql, userDTO.getUsername(), userDTO.getRealname(), userDTO.getRoleId(),
//				userDTO.getLastLoginIp(), userDTO.getLastLoginDate(), userDTO.getCreateDate(), userDTO.getUpdateDate());
//	}

}
