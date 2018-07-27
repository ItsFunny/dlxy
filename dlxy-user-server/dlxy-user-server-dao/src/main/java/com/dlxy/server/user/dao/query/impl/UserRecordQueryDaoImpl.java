/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 下午4:25:44
* 
*/
package com.dlxy.server.user.dao.query.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.server.user.dao.query.UserRecordQueryDao;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月2日 下午4:25:44
 */
@Repository
public class UserRecordQueryDaoImpl implements UserRecordQueryDao
{

	@Autowired
	private QueryRunner queryRunner;

	public static class UserRecordResultSetHandler implements ResultSetHandler<Collection<UserRecordDTO>>
	{

		
//		select a.record_id,a.user_id,b.username,a.record_detail,a.record_detail,a.create_date from dlxy_record a ,dlxy_user b where a.user_id = b.user_id
		@Override
		public Collection<UserRecordDTO> handle(ResultSet rs) throws SQLException
		{
			List<UserRecordDTO> userRecordDTOs = new ArrayList<>();
			while (rs.next())
			{
				UserRecordDTO userRecordDTO = new UserRecordDTO();
				userRecordDTO.setRecordId(rs.getLong(1));
				userRecordDTO.setUserId(rs.getLong(2));
				System.out.println(rs.getString(3));
				userRecordDTO.setRealname(rs.getString(3));
				System.out.println(rs.getString(4));
				userRecordDTO.setRecordDetail(rs.getString(4));
				System.out.println(rs.getObject(5));
				userRecordDTO.setCreateDate((Date)rs.getObject(5));
				userRecordDTOs.add(userRecordDTO);
			}
			return userRecordDTOs;
		}
	}
	// @Override
	// public Long countByParam(Map<String, Object> params) throws SQLException
	// {
	// String sql = "select count(1) from dlxy_article where 1=1 and article_id in
	// (select article_id from dlxy_user_article where user_id = ? ) ";
	// List<Object> l = new LinkedList<Object>();
	// l.add(params.get("userId"));
	// if (params.containsKey("articleStatus"))
	// {
	// sql += "and article_status=? ";
	// l.add(params.get("articleStatus"));
	// }
	// Object count = queryRunner.query(sql, new ScalarHandler<Object>(),
	// l.toArray());
	// if (null == count)
	// {
	// return 0l;
	// } else
	// {
	// return ((Number) count).longValue();
	// }
	// }
	//
	// @Override
	// public List<Map<String, Object>> findByPage(Map<String, Object> params)
	// throws SQLException
	// {
	// List<Object> l = new LinkedList<>();
	// String sql = "select a.username, a,user_id, b.article_id,b.article_name
	// ,b.article_author,b.create_date "
	// + "from dlxy_user_article a , dlxy_article b " + "where 1=1 and
	// a.article_id=b.article_id ";
	// if (params.containsKey("userId"))
	// {
	// sql += " and b.article_id in (select c.article_id from dlxy_user_article c
	// where c.user_id =?) ";
	// l.add(Long.parseLong(params.get("userId").toString()));
	// }
	// if (params.containsKey("articleStatus"))
	// {
	// sql += "and b.article_status = ?";
	// int status = Integer.parseInt(params.get("articleStatus").toString());
	// l.add(status);
	// }
	// sql += " order by b.create_date desc limit ?,?";
	// int start = Integer.parseInt(params.get("start").toString());
	// int end = Integer.parseInt(params.get("end").toString());
	// l.add(start);
	// l.add(end);
	// List<Map<String, Object>> res = queryRunner.query(sql, new MapListHandler(),
	// l.toArray());
	// return res;
	// }

	@Override
	public Collection<UserRecordDTO> findRecordsByPage(int start, int end, Map<String, Object> params)
			throws SQLException
	{
		String sql = "select a.record_id,a.user_id,b.realname,a.record_detail,a.create_date from dlxy_record a ,dlxy_user b where a.user_id = b.user_id ";
		List<Object> l = new LinkedList<Object>();
		String key = null;
//		try
//		{   
//			if (params.containsKey("q"))
//			{
//				key = params.get("q").toString();
//				Long userId = Long.parseLong(key);
//				sql += " and a.user_id = ? ";
//				l.add(userId);
//			}
//		} catch (NumberFormatException e)
//		{
		if(params.containsKey("q"))
		{
			sql += " and b.realname like ? ";
			key=params.get("q").toString();
			l.add("%" + key + "%");
		}else if(params.containsKey("userId"))
		{
			sql+=" and a.user_id= ? ";
			l.add(params.get("userId"));
		}
//		}
		sql += "order by a.create_date desc limit ?,?";
		l.add(start);
		l.add(end);
		Collection<UserRecordDTO> collection = queryRunner.query(sql, new UserRecordResultSetHandler(), l.toArray());
		if (null == collection || collection.isEmpty())
		{
			return Collections.emptyList();
		}
		return collection;
	}

	@Override
	public Long coutRecords(Map<String, Object> params) throws SQLException
	{
		String sql = "select count(1) from dlxy_record where 1=1 ";
		List<Object> l = new LinkedList<>();
		if(params.containsKey("q"))
		{
			sql+=" and  user_id in (select b.user_id from dlxy_user b where b.realname like ? ) ";
			l.add("%"+params.get("q").toString()+"%");
		}else if (params.containsKey("userId"))
		{
			sql += " and user_id = ? ";
			l.add(params.get("userId"));
		}
		Object count = queryRunner.query(sql, new ScalarHandler<Object>(), l.toArray());
		if (null == count)
		{
			return 0l;
		} else
		{
			return ((Number) count).longValue();
		}
	}

}
