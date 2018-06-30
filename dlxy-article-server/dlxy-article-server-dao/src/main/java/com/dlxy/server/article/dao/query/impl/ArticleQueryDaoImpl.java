/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月30日 上午11:45:57
* 
*/
package com.dlxy.server.article.dao.query.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.server.article.dao.query.ArticleQueryDao;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月30日 上午11:45:57
 */
@Repository
public class ArticleQueryDaoImpl implements ArticleQueryDao
{
	public static class ArticleResultSetHandler implements ResultSetHandler<ArticleDTO>
	{

		public ArticleDTO handle(ResultSet rs) throws SQLException
		{
			ArticleDTO articleDTO = new ArticleDTO();
			if (rs.next())
			{
				articleDTO.setArticleId(rs.getLong(0));
				articleDTO.setTitleId(rs.getInt(1));
				articleDTO.setArticleName(rs.getString(2));
				articleDTO.setArticleAuthor(rs.getString(3));
				articleDTO.setArticleIsRecommend(rs.getInt(4));
				articleDTO.setCreateDate(rs.getDate(5));
				articleDTO.setUpdateDate(rs.getDate(6));
				articleDTO.setArticleStatus(rs.getInt(7));
				articleDTO.setUsername(rs.getString(8));
			}
			return articleDTO;
		}
	}

	@Autowired
	private QueryRunner queryRunner;

	public Collection<ArticleDTO> findByParam(Map<String, Object> params, int start, int end) throws SQLException
	{
		StringBuilder sql = new StringBuilder(
				"select a.article_id,a.title_id,a.article_name,a.article_author,a.article_is_recommend,a.create_date,a.update_date,a.article_status ,(select c.username from dlxy_user_article c where c.article_id=a.article_id)username,"
						+ " b.title_name from dlxy_article  a,dlxy_title b "
						+ "where a.title_id=b.title_id ");

		List<Object> set = new LinkedList<Object>();
		if (params.containsKey("articleStatus"))
		{
			if (!StringUtils.isEmpty(params.get("articleStatus").toString()))
			{
				sql.append(" and a.article_status=? ");
				set.add(params.get("articleStatus"));
			}
		} else
		{
			sql.append(" and a.article_status <> 2 ");
		}
		if (params.containsKey("searchParam"))
		{
			if (!StringUtils.isEmpty(params.get("searchParam").toString()))
			{
				sql.append("and ( a.article_name like ? or a.article_id in (select d.article_id from dlxy_user_article d where d.username like ? ))");
				set.add("%" + params.get("searchParam") + "%");
				set.add("%" + params.get("searchParam") + "%");
			}
		}
		// if(!StringUtils.isEmpty(params.get("articleName").toString()))
		// {
		// sql.append(" or a.article_name like ? ");
		// set.add("%"+params.get("articleName")+"%");
		// }
		sql.append(" order by a.create_date desc limit ?,? ");
		set.add(start);
		set.add(end);
		System.out.println(sql);
		List<ArticleDTO> res = queryRunner.query(sql.toString(), new ResultSetHandler<List<ArticleDTO>>()
		{

			public List<ArticleDTO> handle(ResultSet rs) throws SQLException
			{
				List<ArticleDTO> articleDTOs = new LinkedList<ArticleDTO>();
				while (rs.next())
				{
					// select
					// a.article_id,a.title_id,a.article_name,a.article_author,a.article_is_recommend,a.create_date,a.update_date
					// "
					// + ",a.article_status ,c.username
					ArticleDTO articleDTO = new ArticleDTO();
					articleDTO.setArticleId(rs.getLong(1));
					articleDTO.setTitleId(rs.getInt(2));
					articleDTO.setArticleName(rs.getString(3));
					articleDTO.setArticleAuthor(rs.getString(4));
					articleDTO.setArticleIsRecommend(rs.getInt(5));
					articleDTO.setCreateDate(rs.getDate(6));
					articleDTO.setUpdateDate(rs.getDate(7));
					articleDTO.setArticleStatus(rs.getInt(8));
					articleDTO.setUsername(rs.getString(9));
					articleDTOs.add(articleDTO);
				}
				return articleDTOs;
			}
		}, set.toArray());
		return res;
	}

	public Collection<ArticleDTO> findByArticleId(Long articleId) throws SQLException
	{
		String sql = "select a.article_id,a.title_id,a.article_name,a.article_author,a.article_is_recommend,a.create_date,a.update_date ,a.article_status ,b.title_id,b.title_name,c.username "
				+ "from dlxy_article  a,dlxy_title b ,dlxy_user_article c where a.title_id=b.title_id and a.article_id= c.article_id and a.article_id=?";
		ArticleDTO articleDTO = queryRunner.query(sql, new ArticleResultSetHandler(), articleId);
		return Arrays.asList(articleDTO);
	}

}
