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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dlxy.common.dto.ArticleDTO;
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

	@Override
	public Long countByParam(Map<String, Object> params) throws SQLException
	{
		String sql = "select count(1) from dlxy_article where 1=1 and article_id in (select article_id from dlxy_user_article where user_id = ? ) ";
		List<Object> l = new LinkedList<Object>();
		l.add(params.get("userId"));
		if (params.containsKey("articleStatus"))
		{
			sql += "and article_status=? ";
			l.add(params.get("articleStatus"));
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


	@Override
	public List<Map<String, Object>> findByPage(Map<String, Object> params) throws SQLException
	{
		List<Object>l=new LinkedList<>();
		String sql = "select a.username, b.article_id,b.article_name ,b.article_author,b.create_date "
				+ "from dlxy_user_article a , dlxy_article b "
				+ "where a.article_id=b.article_id  and b.article_id in (select c.article_id from dlxy_user_article c where c.user_id =?)";
		l.add(params.get("userId"));
		if(params.containsKey("articleStatus"))
		{
			sql+="and b.article_status = ?";
			int status=Integer.parseInt(params.get("articleStatus").toString());
			l.add(status);
		}
		sql+=" order by b.create_date desc limit ?,?";
		int start = Integer.parseInt(params.get("start").toString());
		int end=Integer.parseInt(params.get("end").toString());
		l.add(start);
		l.add(end);
		List<Map<String, Object>> res = queryRunner.query(sql, new MapListHandler() ,l.toArray());
		return res;
	}

}