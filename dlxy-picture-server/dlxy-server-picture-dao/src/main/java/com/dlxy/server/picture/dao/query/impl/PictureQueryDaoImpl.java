/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午9:01:29
* 
*/
package com.dlxy.server.picture.dao.query.impl;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dlxy.common.dto.PictureDTO;
import com.dlxy.server.picture.dao.query.PictureQueryDao;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月1日 下午9:01:29
*/
@Repository
public class PictureQueryDaoImpl implements PictureQueryDao
{

	@Autowired
	private QueryRunner queryRunner;
//	@Override
//	public void addPicturesInBatch(List<PictureDTO> pictureDTOs) throws SQLException
//	{
//		StringBuilder sql=new StringBuilder("insert into dlxy_picture ( picture_id,picture_url,picture_type) values ");
//		sql.append(StringUtils.repeat("(?,?,?) ,",pictureDTOs.size()-1)).append("(?,?,?)");
//		List<Object>params=new LinkedList<>();
//		pictureDTOs.stream().forEach(pictureDTO->{
//			params.add(pictureDTO.getPictureId());
//			params.add(pictureDTO.getPictureUrl());
//			params.add(pictureDTO.getPictureType());
//		});
//		queryRunner.update(sql.toString(), params.toArray());
//	}
//	@Override
//	public void addPictureWithAricleId(Long articleId, PictureDTO[] pictureDTOs) throws SQLException
//	{
//		StringBuilder sb=new StringBuilder();
//		sb.append("insert into dlxy_article_picture (article_id,picture_id,picture_status) values ");
//		sb.append(StringUtils.repeat("(?,?,?),", pictureDTOs.length-1)).append(" (?,?,?) ");
//		List<Object>list=new LinkedList<>();
//		for (PictureDTO pictureDTO : pictureDTOs)
//		{
//			list.add(pictureDTO.getArticleId());
//			list.add(pictureDTO.getPictureId());
//			list.add(pictureDTO.getPictureStatus());
//		}
//		queryRunner.update(sb.toString(), list.toArray());
//	}
//	@Override
//	public void updatePictureStausInBatch(int status,Long articleId, String[] pictureIds) throws SQLException
//	{
//		List<Object>l=new LinkedList<>();
//		String sql="update dlxy_article_picture set picture_status=?  where article_id = ? ";
//		l.add(status);
//		l.add(articleId);
//		sql+="and picture_id in ("+StringUtils.repeat("?,", pictureIds.length-1)+"? )";
//		for(String s:pictureIds)
//		{
//			l.add(s);
//		}
//		queryRunner.update(sql, l.toArray());
//	}

}
