/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午4:15:26
* 
*/
package com.dlxy.server.article.dao.query.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.server.article.dao.query.TitleQueryDao;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月1日 下午4:15:26
*/
@Repository
public class TitleQueryDaoImpl implements TitleQueryDao
{

	@Autowired
	private QueryRunner queryRunner;
	@Override
	public Collection<DlxyTitleDTO> findByPage(Map<String, Object> params) throws SQLException
	{
		StringBuilder sql=new StringBuilder("select title_id,title_name,title_parent_id,title_display_seq,create_date from dlxy_title where title_parent_id=?");
		List<Object>p=new LinkedList<Object>();
		p.add(params.get("titleParentId"));
		sql.append(" order by title_display_seq desc ");
		if(params.containsKey("start"))
		{
			sql.append(" limit ?,? ");
			p.add(params.get("start"));
			p.add(params.get("end"));
		}
		return queryRunner.query(sql.toString(), (ResultSetHandler<Collection<DlxyTitleDTO>>) rs -> {
			List<DlxyTitleDTO>lists=new ArrayList<>();
			while(rs.next())
			{
				DlxyTitleDTO titleDTO=new DlxyTitleDTO();
				titleDTO.setTitleId(rs.getInt(1));
				titleDTO.setTitleName(rs.getString(2));
				titleDTO.setTitleDisplaySeq(rs.getInt(3));
				titleDTO.setCreateDate(rs.getDate(4));
				lists.add(titleDTO);
			}
			return lists;
		}, p.toArray());
	}
//	@Override
//	public Long countTilte(Map<String, Object> params) throws SQLException
//	{
//		String sql="select count(1) from dlxy_title  where 1=1";
//		List<Object>p=new LinkedList<>();
//		if(params.containsKey("parentTitleId"))
//		{
//			sql+=" and title_parent_id = ? " ;
//			p.add(params.get("parentTitleId"));
//		}
//		Object count = queryRunner.query(sql, new ScalarHandler<Object>(),p.toArray());
//		if(null!=count)
//		{
//			return ((Number)count).longValue();
//		}
//		return 0L;
//	}

}
