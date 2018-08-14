/**
*
* @author joker 
* @date 创建时间：2018年8月13日 上午10:55:37
* 
*/
package com.dlxy.server.user.service;

import java.util.List;

import com.dlxy.server.user.model.DlxyVisitRecord;

/**
* 
* @author joker 
* @date 创建时间：2018年8月13日 上午10:55:37
*/
public interface IVisitRecordService
{
	Integer TOTAL=0;
	Integer PER_DAY=1;
	void update(DlxyVisitRecord visitRecord,Integer visitType);
	
	void addOrUpdate(DlxyVisitRecord visitRecord);
	
	List<DlxyVisitRecord> findByType(Integer type);
	
	DlxyVisitRecord findByRecordDate(Long recordDate);
	
}
