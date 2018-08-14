package com.dlxy.server.user.dao.mybatis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.dlxy.server.user.model.DlxyLink;

@Mapper
public interface LinkDao extends DlxyLinkDao
{
	@Insert("insert into dlxy_link (link_id,link_name,link_url) VALUES (#{linkId},#{linkName},#{linkUrl}) on duplicate key update link_name=VALUES(link_name),link_url=VALUES(link_url)")
	Integer addOrUpdate(DlxyLink link);
}
