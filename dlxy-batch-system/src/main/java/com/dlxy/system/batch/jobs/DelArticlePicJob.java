/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月9日 上午8:35:01
* 
*/
package com.dlxy.system.batch.jobs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月9日 上午8:35:01
*/
@Component
public class DelArticlePicJob implements JobRunner
{
	private Logger logger=LoggerFactory.getLogger(DelArticlePicJob.class);
	
	@Autowired
	private QueryRunner queryRunner;
	
	private class DelArticlePicJobModel 
	{
		private Long articleId;
		private Long pictureId;
		public Long getArticleId()
		{
			return articleId;
		}
		public void setArticleId(Long articleId)
		{
			this.articleId = articleId;
		}
		public Long getPictureId()
		{
			return pictureId;
		}
		public void setPictureId(Long pictureId)
		{
			this.pictureId = pictureId;
		}
	}
	@Override
	public void run()
	{
		String sql="select a.picture_url from dlxy_picture a where a.picture_id in ( select b.picture_id from dlxy_article_picture b where b.article_id is null or ( b.picture_status =0 and datediff(now(),b.create_date)>1) )";
		try
		{
			List<Map<String, Object>> list = queryRunner.query(sql, new MapListHandler());
			List<DelArticlePicJobModel>m=new ArrayList<>();
		
			if(null==list || list.isEmpty())
			{
				logger.info("[batch_delete_pics] dont find any records meeting conditions");
				return;
			}
			//循环执行呢还是另起线程执行
			for (Map<String, Object> map : list)
			{
				String url=(String) map.get("picture_url");
				
			}
			
		} catch (SQLException e)
		{
			logger.error("[batch_delete_pics] occur sql exception");
		}
	}
	
	protected void process(String url)
	{
		url.lastIndexOf(".");
	}

}
