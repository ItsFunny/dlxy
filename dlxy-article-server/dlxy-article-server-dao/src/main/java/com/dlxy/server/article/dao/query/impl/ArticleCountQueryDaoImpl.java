/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午11:14:21
* 
*/
package com.dlxy.server.article.dao.query.impl;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.dlxy.server.article.dao.query.ArticleCountQueryDao;
import com.rabbitmq.client.AMQP.Basic.Return;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月28日 下午11:14:21
 */
@Component
public class ArticleCountQueryDaoImpl implements ArticleCountQueryDao
{

	@Autowired
	private QueryRunner queryRunner;

	public Long coutArticles(Map<String, Object> params) throws SQLException
	{
		String sql = "select count(1) from dlxy_article where 1 =1 ";
		List<Object> p = new LinkedList<Object>();
		if (params.containsKey("articleStatus"))
		{
			if (!StringUtils.isEmpty(params.get("articleStatus").toString()))
			{
				sql += "and article_status=? ";
				p.add(params.get("articleStatus"));
			}
		} else
		{
			sql += " and article_status <> 2 ";
		}
		if (params.containsKey("articleType"))
		{
			if (!StringUtils.isEmpty(params.get("articleType").toString()))
			{
				sql += " and article_type=? ";
				p.add(params.get("articleType"));
			}
		}

		if (params.containsKey("searchParam"))
		{
			if (!StringUtils.isEmpty(params.get("searchParam").toString()))
			{
				// article_id in (select b.article_id from dlxy_user_article b where b.realname
				// like ? )
				sql += " and (article_name like ? or article_author like ? )";
				String tParam = "%" + params.get("searchParam") + "%";
				p.add(tParam);
				p.add(tParam);
			}
		}
		if (params.containsKey("userId"))
		{
			if (!StringUtils.isEmpty(params.get("userId").toString()))
			{
				sql += "and article_id in (select b.article_id from dlxy_user_article b where exists(select 1 from dlxy_user where user_id = ? ) and b.user_id= ? ) ";
				p.add(params.get("userId"));
				p.add(params.get("userId"));
			}
		}

		Object count = queryRunner.query(sql, new ScalarHandler<Object>(), p.toArray());
		if (null == count)
		{
			return 0L;
		} else
		{
			return ((Number) count).longValue();
		}
	}

	@Override
	public Long countTitleArticles(Integer titleId, Integer parentTitleId, Integer status) throws SQLException
	{
		/*
		 * 1.请求的是一个子类    那样只需要查询子类所拥有的记录数:titleId
		 */
		String sql = "select count(1) from dlxy_article where 1=1 ";
		List<Object> l = new LinkedList<>();
		if (0 != parentTitleId)
		{
			sql += " and title_id= ? ";
			l.add(titleId);
			
		} else
		{
			sql += " and title_id in ( select b.title_id from dlxy_title b where b.title_parent_id = ? or b.title_id=? )";
			l.add(titleId);
			l.add(titleId);
		}
		sql += " and article_status= ? ";
		l.add(status);
		Object o = queryRunner.query(sql, new ScalarHandler<Object>(), l.toArray());
		if (null == o)
		{
			return 0l;
		} else
		{
			return ((Number) o).longValue();
		}
	}
}
