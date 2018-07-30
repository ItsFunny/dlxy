/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月27日 下午3:07:25
* 
*/
package com.dlxy.system.batch.jobs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.model.ArticleVisitInfo;
import com.dlxy.common.utils.JsonUtil;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.system.batch.service.IRedisService;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月27日 下午3:07:25
 */
@Component
public class ArticleVisitCountJob implements JobRunner
{
	public static final LinkedBlockingQueue<String> ARTICLEIDS = new LinkedBlockingQueue<>();
	// static
	// {
	// ARTICLEIDS.add("386208411710259200");
	// ARTICLEIDS.add("384053948924952576");
	// }

	@Autowired
	private IRedisService redisService;

	@Autowired
	private IArticleService articleService;
	private Logger logger = LoggerFactory.getLogger(ArticleVisitCountJob.class);

	//每隔9天执行一次
	@Scheduled(cron = "0/10 0 0 1-12 * ? ")
	@Override
	public void run()
	{
		Integer count = 0;
		Set<ArticleDTO> articleDTOs = new HashSet<>();
		Long beginTime = System.currentTimeMillis();
		logger.info("[ArticleVisitCountJob] begin update articleVistitCoutns ");
		try
		{
			synchronized (ARTICLEIDS)
			{
				for (String string : ARTICLEIDS)
				{
					String key = String.format(IRedisService.ARTICLE_VISIT_COUNT, string);
					String json = redisService.get(key);
					if (!StringUtils.isEmpty(json))
					{
						ArticleVisitInfo articleVisitInfo = JsonUtil.json2Object(json, ArticleVisitInfo.class);
						ArticleDTO articleDTO = new ArticleDTO();
						articleDTO.setArticleId(Long.parseLong(string));
						articleDTO.setVisitCount(articleVisitInfo.getVisitCount().get());
						articleDTOs.add(articleDTO);
					}
				}
				ARTICLEIDS.clear();
			}
			//这里最好是调用有事务的方法,异常了回滚然后
			if (!articleDTOs.isEmpty())
			{
				try
				{
					count = articleService.updateInBatchSelective(articleDTOs);
				} catch (Exception e)
				{
					logger.error("[ArticleVisitCountJob] occur sql error : {}",e.getMessage());
					//将这些单独的放在一个list中,别放回去,不然还是会错,但是放回去也行,因为通常都是sql拥挤的错误
					//只要数据库不蹦,基本不会捕获到这个异常
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("[ArticleVisitCountJob] occur error {}", e.getMessage());
		}
		logger.info("[ArticleVisitCountJob] finished  job ,effective count : {},cost {} millseconds", count,
				System.currentTimeMillis() - beginTime);
	}

}
