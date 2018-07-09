/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月4日 下午5:32:46
* 
*/
package com.dlxy.server.user.dao.query.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dlxy.server.user.dao.query.UserArticleQueryDao;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月4日 下午5:32:46
 */
@Repository
public class UserArticleQueryDaoImpl implements UserArticleQueryDao
{
	@Autowired
	private QueryRunner queryRunner;

	@Override
	public Long countByParam(Map<String, Object> params) throws SQLException
	{
		String sql = "select count(1) from dlxy_article where 1=1 ";
		List<Object> l = new LinkedList<Object>();
	
//		if(params.containsKey("searchParam"))
//		{
//			String q=params.get("searchParam").toString();
//			try
//			{
//				long userId=Long.parseLong(q);
//				sql+=" and user_id = ?";
//				l.add(params.get("searchParam"));
//			} catch (NumberFormatException e)
//			{
//				sql+=" and username like ? ";
//				l.add("%"+params.get("searchParam")+"%");
//			}
//		}
		if(params.containsKey("userId"))
		{
			sql+=" and article_id in (select article_id from dlxy_user_article where 1=1   and user_id = ? ) ";
			l.add(params.get("userId"));
		}
		
		if(params.containsKey("nameLike"))
		{
			sql+=" and article_id in (select article_id from dlxy_user_article where 1=1   and username like ? ) ";
			l.add("%"+params.get("nameLike")+"%");
		}
		if (params.containsKey("articleStatus"))
		{
			sql += "and article_status=? ";
			l.add(params.get("articleStatus"));
		}
//		if(params.containsKey(""))
//		{
//			sql+="";
//			l.add(params.get(""));
//		}
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
		List<Object> l = new LinkedList<>();
		String sql = "select a.username, b.article_status, a.user_id, b.article_id,b.article_name ,b.article_author,b.create_date ,b.title_id ,b.update_date,b.delete_date,d.title_name "
				+ " from dlxy_user_article a , dlxy_article b ,dlxy_title d" + " where 1=1 and a.article_id=b.article_id and b.title_id = d.title_id ";
		if (params.containsKey("userId"))
		{
			sql += " and b.article_id in (select c.article_id from dlxy_user_article c where c.user_id =?) ";
			l.add(params.get("userId"));
		}
		if(params.containsKey("username"))
		{
			sql+="and b.article_id in (select c.article_id from dlxy_user_article c where c.username like ?) ";
			l.add("%"+params.get("username")+"%");
		}
		if (params.containsKey("articleStatus"))
		{
			sql += "and b.article_status = ?";
			int status = Integer.parseInt(params.get("articleStatus").toString());
			l.add(status);
		}
		sql += " order by b.create_date desc limit ?,?";
		int start = Integer.parseInt(params.get("start").toString());
		int end = Integer.parseInt(params.get("end").toString());
		l.add(start);
		l.add(end);
		List<Map<String, Object>> res = queryRunner.query(sql, new MapListHandler(), l.toArray());
		return res;
	}

}
