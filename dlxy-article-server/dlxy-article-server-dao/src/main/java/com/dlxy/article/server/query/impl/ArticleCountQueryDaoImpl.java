/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午11:14:21
* 
*/
package com.dlxy.article.server.query.impl;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dlxy.article.server.query.ArticleCountQueryDao;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月28日 下午11:14:21
 */
@Repository
public class ArticleCountQueryDaoImpl implements ArticleCountQueryDao
{

	@Autowired
	private QueryRunner queryRunner;

	public Long coutArticles(Map<String, String> params) throws SQLException
	{
		String sql = "select count(1) from dlxy_article where 1 =1 ";
		List<Object> p = new LinkedList<Object>();
		if (!StringUtils.isEmpty(params.get("articleStatus")))
		{
			sql += "and article_status=? ";
			p.add(params.get("articleStatus"));
		}
		if (!StringUtils.isEmpty(params.get("articleIsRecommend")))
		{
			sql += "and article_is_recommend=? ";
			p.add(params.get("articleIsRecommend"));
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
}
