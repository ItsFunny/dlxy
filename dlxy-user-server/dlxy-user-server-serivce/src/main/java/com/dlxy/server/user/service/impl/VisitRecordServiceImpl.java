/**
*
* @author joker 
* @date 创建时间：2018年8月13日 上午10:58:04
* 
*/
package com.dlxy.server.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.common.vo.CreaterAndConsumer;
import com.dlxy.server.user.dao.mybatis.DlxyVisitRecordDao;
import com.dlxy.server.user.dao.mybatis.VisitRecordDao;
import com.dlxy.server.user.model.DlxyVisitRecord;
import com.dlxy.server.user.model.DlxyVisitRecordExample;
import com.dlxy.server.user.model.DlxyVisitRecordExample.Criteria;
import com.dlxy.server.user.service.IVisitRecordService;
import com.joker.library.utils.DateUtils;

/**
* 
* @author joker 
* @date 创建时间：2018年8月13日 上午10:58:04
*/
@Service
public class VisitRecordServiceImpl implements IVisitRecordService
{
	
	@Autowired
	private VisitRecordDao visitRecordDao;

	@Override
	public void update(DlxyVisitRecord visitRecord,Integer type)
	{
		DlxyVisitRecordExample example=new DlxyVisitRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andVisitRecordTypeEqualTo(type);
		visitRecord.setVisitRecordDate(DateUtils.getCurrentDay());
		visitRecordDao.updateByExampleSelective(visitRecord, example);
	}

	@Override
	public void addOrUpdate(DlxyVisitRecord visitRecord)
	{
//		DlxyVisitRecordExample example=new DlxyVisitRecordExample();
//		Criteria criteria = example.createCriteria();
		visitRecord.setVisitRecordDate(DateUtils.getCurrentDay());
		visitRecordDao.addOrUpdate(visitRecord);
	}

	@Override
	public List<DlxyVisitRecord> findByType(Integer type)
	{
		DlxyVisitRecordExample example=new DlxyVisitRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andVisitRecordTypeEqualTo(type);
		return visitRecordDao.selectByExample(example);
	}

	@Override
	public DlxyVisitRecord findByRecordDate(Long recordDate)
	{
		DlxyVisitRecordExample example=new DlxyVisitRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andVisitRecordDateEqualTo(recordDate);
		List<DlxyVisitRecord> res = visitRecordDao.selectByExample(example);
		if(null==res|| res.isEmpty())
		{
			return null;
		}else if(res.size()==1)
		{
			return res.iterator().next();
		}else {
			throw new RuntimeException("find multi records");
		}
	}

}
