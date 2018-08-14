package com.dlxy.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.dlxy.model.ArticleVisitCountFactory;
import com.dlxy.server.user.model.DlxyVisitRecord;
import com.dlxy.server.user.service.IVisitRecordService;
import com.dlxy.service.IRedisService;
import com.joker.library.utils.DateUtils;

@Component
public class DailyUpdateCountJob implements Runnable
{
	private Logger logger=LoggerFactory.getLogger(DailyUpdateCountJob.class);
	@Autowired
	private IRedisService redisService;
	
	@Autowired
	private IVisitRecordService visitRecordService;
	
	//每天23点更新
	@Scheduled(cron="0 0 23 1-30 1-12 ? ")
	@Override
	public void run()
	{
		ArticleVisitCountFactory.refreshStorage();
		logger.info("Begin dailyUpdateVisitCount job ");
		Long startTime=System.currentTimeMillis();
		try
		{
			String totalCount = redisService.get(IRedisService.WEB_VISIT_TOTAL_COUT);
			if(StringUtils.isEmpty(totalCount))
			{
				logger.error("the web total visit count is empty ");
			}else {
				DlxyVisitRecord visitRecord=new DlxyVisitRecord();
				visitRecord.setVisitCount(Integer.parseInt(totalCount));
				visitRecordService.update(visitRecord, IVisitRecordService.TOTAL);
			}
			String perDayVisit = redisService.get(String.format(IRedisService.PER_DAY_VISIT_COUNT, DateUtils.getCurrentDay()));
			if(StringUtils.isEmpty(perDayVisit))
			{
				logger.error("the web total visit count is empty ,may no one has visited");
			}else {
				DlxyVisitRecord visitRecord=new DlxyVisitRecord();
				visitRecord.setVisitCount(Integer.parseInt(perDayVisit));
				visitRecord.setVisitRecordType(IVisitRecordService.PER_DAY);
				visitRecordService.addOrUpdate(visitRecord);
			}
		} catch (Exception e)
		{
			logger.error("DailyUpdateVisitCount job fail for reason :{} ,may the redis is crushed",e.getMessage());
		}
		logger.info("dailyUpdateVisitCount finish the job ,consume {} seconds",(System.currentTimeMillis()-startTime)/1000);
	}

}
