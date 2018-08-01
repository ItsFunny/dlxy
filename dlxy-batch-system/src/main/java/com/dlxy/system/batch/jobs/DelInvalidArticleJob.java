/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月31日 下午12:41:25
* 
*/
package com.dlxy.system.batch.jobs;

import java.io.File;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dlxy.common.dto.ResultDTO;
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.common.utils.FileUtil;
import com.dlxy.common.utils.RSAUtils;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.picture.service.IPictureService;
import com.dlxy.system.batch.config.DlxyProperty;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月31日 下午12:41:25
 */
//@Component
public class DelInvalidArticleJob implements JobRunner
{
	@Autowired
	private QueryRunner queryRunner;
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private DlxyProperty dlxyProperty;
	@Autowired
	private IPictureService pictureService;
	@Autowired
	private IArticleService articleService;

	private String picStoreUrl;

	private String getStoreUrl()
	{
		if (StringUtils.isEmpty(picStoreUrl))
		{
			try
			{
				String token = RSAUtils.encryptByPublic("getImgsAddress", dlxyProperty.getPublicKeyBytes());
				@SuppressWarnings("rawtypes")
				ResultDTO resultDTO = restTemplate.getForObject(
						"http://localhost:8000/api/v1/address/images.html?token=" + URLEncoder.encode(token, "utf-8"),
						ResultDTO.class);
				if (resultDTO.getCode() == 1)
				{
					picStoreUrl = (String) resultDTO.getData();
					return picStoreUrl;
				} else
				{
					logger.error("[rest请求存放图片地址出错]error:{}", resultDTO.getMsg());
					return null;
				}
			} catch (Exception e)
			{
				logger.error("[获取图片存储地址错误]error:{}", e.getMessage());
				return null;
			}
		} else
		{
			return picStoreUrl;
		}
	}

	private Logger logger = LoggerFactory.getLogger(DelInvalidArticleJob.class);

	@Scheduled(cron = "0/10 * * * * ?")
	@Override
	public void run()
	{
		Long beginTime = System.currentTimeMillis();
		logger.info("[Delete redundant articles] begin the job");
		Set<Long> ids = new HashSet<Long>();

		List<Long> backUpdateIds = new ArrayList<>();
		List<Long> deleteIds = new ArrayList<>();
		Integer count = 0;
		// 这条sql per上有问题,但是这个项目上应该dont care
		String sql = "SELECT article_id,delete_date FROM dlxy_article WHERE article_status=2 ";
		try
		{
			List<Map<String, Object>> res = queryRunner.query(sql, new MapListHandler());
			if (res != null && !res.isEmpty())
			{
				for (Map<String, Object> map : res)
				{
					ids.add(Long.parseLong(map.get("article_id").toString()));
				}
			}
			if (ids.isEmpty())
			{
				logger.info("[DeleteInvalidArticle] find no match records  finish the job ");
				return;
			}
			String storeUrl = getStoreUrl();
			if (StringUtils.isEmpty(storeUrl))
			{
				logger.error("[DeleteInvalidArticles] finished unexpceted");
				return;
			}
			for (Long articleId : ids)
			{
				File file = null;
				try
				{
					file = new File(storeUrl + File.separator + "imgs" + File.separator + articleId);
					boolean isDel = FileUtil.delFileOrDir(file);
					if (isDel)
					{
						deleteIds.add(articleId);
					} else
					{
						backUpdateIds.add(articleId);
					}
				} catch (Exception e)
				{
					logger.error("[删除文章旗下的图片失败],file:{}", file);
					backUpdateIds.add(articleId);
				}
			}
			if (!deleteIds.isEmpty())
			{
				count = articleService.deleteArticlesInBatch(new ArrayList<Long>(ids));
				try
				{
					delPicsByArticleIds(deleteIds);
				} catch (SQLException e)
				{
					logger.error("[DeleteInvalidArticles]error occured in db ,error:{},cause:{}", e.getMessage(),
							e.getCause());
					backUpdateIds.addAll(deleteIds);
				}
			}
			if (!backUpdateIds.isEmpty())
			{
				pictureService.updateArticlePictureStatusByArticleIdsInbatch(
						backUpdateIds.toArray(new Long[backUpdateIds.size()]), PictureStatusEnum.Invalid.ordinal());
			}
		} catch (SQLException e)
		{
			logger.error("[DeleteinvalidArticles] occur sql error:{},cause:{}", e.getMessage(), e.getCause());
			return;
		} catch (Exception e)
		{
			logger.error("[DeleteinvalidArticles] occur  error:{},cause:{}", e.getMessage(), e.getCause());
			return;
		}
		logger.info("[DeleteInvalidArticleJob] finish the job,delete {} records,consume {} millseconds", count,
				System.currentTimeMillis() - beginTime);
	}

	private Integer delPicsByArticleIds(List<Long> articleIds) throws SQLException
	{
		StringBuilder sql = new StringBuilder(
				" delete from article_picture where picture_id in ( select picture_id from dlxy_artile_picture where article_id in (  ");
		sql.append(StringUtils.repeat(" ?,", articleIds.size() - 1));
		sql.append(" ? )");
		Object query = queryRunner.query(sql.toString(), new ScalarHandler<Object>(), articleIds.toArray());
		if (query != null)
		{
			return ((Number) query).intValue();
		}
		return 0;
	}

}
