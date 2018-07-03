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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRecordDTO;
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
			needPwd=i;
		}
		@Override
		public Collection<UserDTO> handle(ResultSet rs) throws SQLException
		{
			List<UserDTO>userDTOs=new ArrayList<UserDTO>();
			while(rs.next())
			{
				UserDTO userDTO=new UserDTO();
				
			}
			return null;
		}
		
	}
	@Override
	public UserDTO findByNameOrId(String key) throws SQLException
	{
		String sql="select a.user_id,a.username,a.realname,a.passwrod,a.role_id,a.last_login_ip,a.crete_date,a.update_date from dlxy_user a where 1=1 ";
		List<Object>l=new LinkedList<>();
		try
		{
			Long userId=Long.parseLong(key);
			sql+=" and a.user_id = ? ";
			l.add(userId);
		} catch (NumberFormatException e)
		{
			sql+=" and a.username like ? ";
			l.add("%"+key+"%");
		}
		Collection<UserDTO> query = queryRunner.query(sql, new UserResultSetHandler(true), l.toArray());
		if(null==query)
		{
			return null;
		}else if(query.size()==1)
		{
			return query.iterator().next();
		}else {
			 throw new RuntimeException("find multipart users ");
		}
	}

}
