package com.dlxy.server.user.dao.mybatis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.dlxy.server.user.model.DlxyVisitRecord;

@Mapper
public interface VisitRecordDao extends DlxyVisitRecordDao
{
	@Insert("INSERT INTO dlxy_visit_record (visit_id,visit_record_type,visit_count,visit_record_date) VALUES (#{visitId},#{visitRecordType},#{visitCount},#{visitRecordDate}) ON DUPLICATE KEY UPDATE visit_count=VALUES (visit_count) ")
	void addOrUpdate(DlxyVisitRecord record);
}
