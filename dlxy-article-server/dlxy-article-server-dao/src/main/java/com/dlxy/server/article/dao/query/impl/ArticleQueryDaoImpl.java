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
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.enums.ArticleTypeEnum;
import com.dlxy.server.article.dao.mybatis.ArticleMybatisDao;
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
	@Autowired
	private ArticleMybatisDao articleDao;

	public static class ArticleNormalResultSetHandler implements ResultSetHandler<List<ArticleDTO>>
	{

		@Override
		public List<ArticleDTO> handle(ResultSet rs) throws SQLException
		{
			List<ArticleDTO> articleDTOs = new ArrayList<>();
			while (rs.next())
			{
				ArticleDTO articleDTO = new ArticleDTO();
				articleDTO.setArticleId(rs.getLong(1));
				articleDTO.setTitleId(rs.getInt(2));
				articleDTO.setArticleName(rs.getString(3));
				articleDTO.setArticleAuthor(rs.getString(4));
				articleDTO.setArticleType(rs.getInt(5));
				articleDTO.setCreateDate((Date)rs.getObject(6));
				articleDTO.setUpdateDate((Date)rs.getObject(7));
				articleDTO.setArticleStatus(rs.getInt(8));
				articleDTO.setRealname(rs.getString(9));
				articleDTO.setUserId(rs.getLong(10));
				articleDTO.setDeleteDate((Date)rs.getObject(11));
				articleDTO.setTitleName(rs.getString(12));
				articleDTOs.add(articleDTO);
			}
			return articleDTOs;
		}
	}

	public static class ArticleTypeResultSetHandler implements ResultSetHandler<List<ArticleDTO>>
	{
		@Override
		public List<ArticleDTO> handle(ResultSet rs) throws SQLException
		{
			List<ArticleDTO> articleDTOs = new ArrayList<>();
			while (rs.next())
			{
				ArticleDTO articleDTO = new ArticleDTO();
				articleDTO.setArticleId(rs.getLong(1));
				articleDTO.setTitleId(rs.getInt(2));
				articleDTO.setArticleName(rs.getString(3));
				articleDTO.setArticleAuthor(rs.getString(4));
				articleDTO.setArticleType(rs.getInt(5));
				articleDTO.setCreateDate((Date)rs.getObject(6));
				articleDTO.setUpdateDate((Date)rs.getObject(7));
				articleDTO.setArticleStatus(rs.getInt(8));
				articleDTO.setRealname(rs.getString(9));
				articleDTO.setUserId(rs.getLong(10));
				articleDTO.setDeleteDate((Date)rs.getObject(11));
				articleDTO.setTitleName(rs.getString(12));
				articleDTO.setPictureUrl(rs.getString(13));
				articleDTOs.add(articleDTO);
			}

			return articleDTOs;
		}

	}

	@Autowired
	private QueryRunner queryRunner;

	public Collection<ArticleDTO> findByParam(Map<String, Object> params, int start, int end) throws SQLException
	{
		StringBuilder sql = new StringBuilder();
		ResultSetHandler<List<ArticleDTO>> resultSetHandler = null;

		boolean isTypeFind = false;
		if (params.containsKey("articleType")
				&& Integer.parseInt(params.get("articleType").toString()) == ArticleTypeEnum.PICTURE_ARTICLE.ordinal())
		{
			sql.append(
					"SELECT a.article_id,a.title_id,a.article_name,a.article_author,a.article_type,a.create_date,a.update_date,a.article_status,c.realname,c.user_id,a.delete_date,b.title_name,e.picture_url FROM dlxy_article a LEFT JOIN dlxy_title b ON a.title_id=b.title_id LEFT JOIN dlxy_user_article c ON a.article_id=c.article_id LEFT JOIN dlxy_article_picture d ON d.article_id=a.article_id AND d.picture_type=1 AND d.picture_status =1 LEFT JOIN dlxy_picture e ON d.picture_id=e.picture_id WHERE 1=1 ");
			isTypeFind = true;
			resultSetHandler = new ArticleTypeResultSetHandler();
		} else
		{
			sql.append(
					"select a.article_id,a.title_id,a.article_name,a.article_author,a.article_type,a.create_date,a.update_date,a.article_status ,c.realname,c.user_id,a.delete_date,b.title_name ");
			sql.append(
					"from dlxy_article  a left join dlxy_title b on a.title_id=b.title_id left join dlxy_user_article c on a.article_id=c.article_id where 1=1 ");
			resultSetHandler = new ArticleNormalResultSetHandler();
		}
		// StringBuilder sql = new StringBuilder(
		// "select
		// a.article_id,a.title_id,a.article_name,a.article_author,a.article_type,a.create_date,a.update_date,a.article_status
		// ,c.realname,c.user_id,a.delete_date,b.title_name ");
		// sql.append(
		// "from dlxy_article a left join dlxy_title b on a.title_id=b.title_id left
		// join dlxy_user_article c on a.article_id=c.article_id where 1=1 ");
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
		if (params.containsKey("articleType"))
		{
			sql.append(" and a.article_type=? ");
			set.add(params.get("articleType"));
		}
		if (params.containsKey("searchParam"))
		{
			if (!StringUtils.isEmpty(params.get("searchParam").toString()))
			{
				// 这里好像有问题,这样写好像如果知道userId 对应的realname 照样能获取到某个user的文章信息
				// 2018-07-07 22:47 直接屏蔽 对realname的判断
				// or a.article_id in (select d.article_id from dlxy_user_article d where
				// d.realname like ? )
				sql.append("and ( a.article_name like ? or a.article_author like ? )");
				set.add("%" + params.get("searchParam") + "%");
				set.add("%" + params.get("searchParam") + "%");
			}
		}else if(params.containsKey("articleId"))
		{
			sql.append(" and a.article_id = ? ");
			set.add(params.get("articleId"));
		}
		if (params.containsKey("titleId"))
		{
			sql.append(" and a.title_id = ? ");
			set.add(params.get("titleId"));
		} else if (params.containsKey("titleParentId"))
		{
			sql.append(" and a.title_id in ( select g.title_id from dlxy_title g where g.title_parent_id = ? ) ");
			set.add(params.get("titleParentId"));
		}
		if (params.containsKey("userId"))
		{
			sql.append(
					" and a.article_id in ( select e.article_id from dlxy_user_article e where exists(select 1 from dlxy_user f where f.user_id = ? ) and e.user_id = ? )");
			set.add(params.get("userId"));
			set.add(params.get("userId"));
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
		List<ArticleDTO> res = queryRunner.query(sql.toString(), resultSetHandler, set.toArray());
		return res;
	}

	// public ArticleDTO findByArticleId(Long articleId) throws SQLException
	// {
	// String sql = "select
	// a.article_id,a.title_id,a.article_name,a.article_author,a.article_type,a.create_date,a.update_date
	// ,a.article_status ,c.realname ,c.user_id,a.delete_date,a.article_content "
	// + " from dlxy_article a,dlxy_title b ,dlxy_user_article c where
	// a.title_id=b.title_id and a.article_id= c.article_id and a.article_id=?";
	// ArticleDTO query = queryRunner.query(sql, new
	// ArticleSingleResultSetHandler(), articleId);
	// return query;
	// }

	// @Override
	// public int rollBackArticle(int status, Long articleId, Integer titleId)
	// throws SQLException
	// {
	// String sql = "update dlxy_article set article_status=? where article_id=? and
	// exists(select 1 from dlxy_title b where b.title_id = ? )";
	// return queryRunner.update(sql, status, articleId, titleId);
	// }

	@Override
	public void update(ArticleDTO articleDTO) throws SQLException
	{
		if (null == articleDTO.getArticleId() || articleDTO.getArticleId() <= 0)
		{
			throw new RuntimeException("article id is required ");
		}
//		ArticleDTO dbArticleDTO = articleDao.findArticleDetailByArticleId(articleDTO.getArticleId());
		ArticleDTO dbArticleDTO=articleDao.selectByPrimaryKey(articleDTO.getArticleId());
		StringBuilder sb = new StringBuilder();
		List<Object> l = new LinkedList<>();
		sb.append("update dlxy_article set update_date=?");
		l.add(new Date());
		if (null != articleDTO.getArticleName() && !articleDTO.getArticleName().equals(dbArticleDTO.getArticleName()))
		{
			sb.append(" , article_name = ? ");
			l.add(articleDTO.getArticleName());
		}
		if (null != articleDTO.getArticleId() && !articleDTO.getArticleId().equals(dbArticleDTO.getArticleId()))
		{
			sb.append(" ,title_id=? ");
			l.add(articleDTO.getTitleId());
		}
		if (!StringUtils.isEmpty(articleDTO.getArticleAuthor())
				&& !articleDTO.getArticleAuthor().equals(dbArticleDTO.getArticleAuthor()))
		{
			sb.append(" , article_author= ? ");
			l.add(articleDTO.getArticleAuthor());
		}
		if (!StringUtils.isEmpty(articleDTO.getArticleContent())
				&& !articleDTO.getArticleContent().equals(dbArticleDTO.getArticleContent()))
		{
			sb.append(" , article_content= ? ");
			l.add(articleDTO.getArticleContent());
		}
		if (null != articleDTO.getArticleType() && !articleDTO.getArticleType().equals(dbArticleDTO.getArticleType()))
		{
			sb.append(" , article_type = ? ");
			l.add(articleDTO.getArticleType());
		}
		if (null != articleDTO.getArticleStatus()
				&& !articleDTO.getArticleStatus().equals(dbArticleDTO.getArticleStatus()))
		{
			sb.append(" , article_status = ? ");
			l.add(articleDTO.getArticleStatus());
		}
		if (null != articleDTO.getTitleId() && !articleDTO.getTitleId().equals(dbArticleDTO.getTitleId()))
		{
			sb.append(" , title_id = ? ");
			l.add(articleDTO.getTitleId());
		}
		sb.append(" where article_id = ? ");
		l.add(articleDTO.getArticleId());
		queryRunner.update(sb.toString(), l.toArray());
	}

	// @Override
	// public Collection<ArticleDTO> findArticlesInTitleIds(List<Integer> titelIds,
	// int limit) throws SQLException
	// {
	// StringBuilder sql = new StringBuilder(
	// " SELECT a.article_id,article_name,a.create_date,b.title_parent_id FROM
	// dlxy_article a,dlxy_title b WHERE a.title_id=b.title_id AND
	// a.article_status<> 2 AND a.title_id IN ( ");
	// sql.append(" select b.title_id from dlxy_title b where b.title_parent_id in (
	// ");
	// sql.append(StringUtils.repeat(" ?, ", titelIds.size()-1)).append(" ? ) ");
	// sql.append(" ) ");
	// return queryRunner.query(sql.toString(), new
	// ResultSetHandler<Collection<ArticleDTO>>()
	// {
	//
	// @Override
	// public Collection<ArticleDTO> handle(ResultSet rs) throws SQLException
	// {
	// List<ArticleDTO>articleDTOs=new ArrayList<ArticleDTO>();
	// while(rs.next())
	// {
	// ArticleDTO articleDTO=new ArticleDTO();
	// articleDTO.setArticleId(rs.getLong(1));
	// articleDTO.setArticleName(rs.getString(2));
	// articleDTO.setCreateDate((Date)rs.getDate(3));
	// articleDTO.setTitleParentId(rs.getInt(4));
	// articleDTOs.add(articleDTO);
	// }
	// return articleDTOs;
	// }
	// }, titelIds.toArray());
	// }

	@Override
	public Collection<ArticleDTO> findArticlesInTitleIds(Map<String, Object> params) throws SQLException
	{
		List<Object> l = new LinkedList<>();
		List<Integer> titleIds = (List<Integer>) params.get("ids");
		StringBuilder sql = new StringBuilder(
				" SELECT a.article_id,article_name,a.create_date,b.title_parent_id,a.article_author FROM dlxy_article a,dlxy_title b WHERE a.title_id=b.title_id AND a.article_status<> 2 AND a.title_id IN ( ");
		sql.append(" select b.title_id from dlxy_title b where b.title_parent_id in ( ");
		sql.append(StringUtils.repeat(" ?, ", titleIds.size() - 1)).append(" ? ) ");
		sql.append(" ) ");
		l.addAll(titleIds);
		sql.append("order by a.create_date desc ");
		if (params.containsKey("start") || params.containsKey("end"))
		{
			sql.append(" limit ?, ? ");
			l.add(params.get("start"));
			l.add(params.get("end"));
		} else
		{
			sql.append("limit ? ");
			l.add(params.get("limit"));
		}
		return queryRunner.query(sql.toString(), new ResultSetHandler<Collection<ArticleDTO>>()
		{
			@Override
			public Collection<ArticleDTO> handle(ResultSet rs) throws SQLException
			{
				List<ArticleDTO> articleDTOs = new ArrayList<ArticleDTO>();
				while (rs.next())
				{
					ArticleDTO articleDTO = new ArticleDTO();
					articleDTO.setArticleId(rs.getLong(1));
					articleDTO.setArticleName(rs.getString(2));
					articleDTO.setCreateDate((Date) rs.getDate(3));
					articleDTO.setTitleParentId(rs.getInt(4));
					articleDTOs.add(articleDTO);
				}
				return articleDTOs;
			}
		}, l.toArray());
	}

}
