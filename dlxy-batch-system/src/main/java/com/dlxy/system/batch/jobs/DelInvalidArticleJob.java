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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
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
import com.dlxy.common.utils.RSAUtils;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.picture.service.IPictureService;
import com.dlxy.system.batch.config.DlxyProperty;
import com.dlxy.system.batch.service.AbstractDeleteFileHandler;
import com.dlxy.system.batch.service.DefaultFileStrategyHandler;
import com.dlxy.system.batch.service.FTPFileStrategyHandler;
import com.dlxy.system.batch.service.IFileDeleteHandler;
import com.joker.library.file.IFileStrategy;

/**
 * 删除放到回收站的文章 结合消息队列做是最好的,可以控制在一个稳定的时间内,但是问题不大
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月02日 下午12:41:25
 */
 @Component
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

	@Autowired
	private IFileDeleteHandler deleteHandler;

//	@Autowired
//	private FileStrategyContext fileStrategyContext;

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
						"http://www.jokerlvccc.club/api/v1/address/images.html?token=" + URLEncoder.encode(token, "utf-8"),
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

	// 没10天的23点执行一次
	@Scheduled(cron = "0 0 23 1/10 1-12 ? ")
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
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -10);
		Long limitTime = Long.parseLong(simpleDateFormat.format(calendar.getTime()));
		String sql = "SELECT article_id,delete_time FROM dlxy_article WHERE delete_time BETWEEN 19700102 AND ? AND article_status=2  ";

		try
		{
			List<Map<String, Object>> res = queryRunner.query(sql, new MapListHandler(), limitTime);
			if (res != null && !res.isEmpty())
			{
				for (Map<String, Object> map : res)
				{
					ids.add(Long.parseLong(map.get("article_id").toString()));
				}
			}
			if (ids.isEmpty())
			{
				logger.info("[DeleteInvalidArticle] find no match records ,finish the job ");
				return;
			}
			String storeUrl = getStoreUrl();
			if (StringUtils.isEmpty(storeUrl))
			{
				logger.error(
						"[DeleteInvalidArticles] finished unexpceted,error: the img store url is empty for rest error");
				return;
			}
			for (Long articleId : ids)
			{
				File file = null;
				try
				{
					// 指定存储方式的话直接这样既可,只需要注入bean的时候指定方式既可
					// Boolean isSuccess =
					// fileStrategyContext.delete(fileStrategyContext.getStoreBasePath(IFileStrategy.IMG_TYPE)
					// + File.separator + fileStrategyContext.getVisitPrefix(IFileStrategy.IMG_TYPE)
					// + File.separator + articleId);
					// 实际是会指定的,但本地可以先这么写
					boolean isDel = loopDelete(deleteHandler, articleId);
//					file = new File(storeUrl + File.separator + "imgs" + File.separator + articleId);
//					boolean isDel = FileUtil.delFileOrDir(file);
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
				pictureService.updateArticlePictureStatusByArticleIdsInbatch(backUpdateIds,
						PictureStatusEnum.Invalid.ordinal());
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

	private boolean loopDelete(IFileDeleteHandler deleteHandler, Long articleId)
	{
		boolean res = false;
		AbstractDeleteFileHandler absDFH = (AbstractDeleteFileHandler) deleteHandler;
		IFileDeleteHandler nextHandler = absDFH.getNextHandler();
		IFileDeleteHandler.DeleteHandlerObject deleteHandlerObject = new IFileDeleteHandler.DeleteHandlerObject();
		String dbUrl = absDFH.getBaseStorePath(IFileStrategy.IMG_TYPE) + File.separator
				+ absDFH.getVisitPrefix(IFileStrategy.IMG_TYPE) + File.separator + articleId;
		deleteHandlerObject.setDbUrl(dbUrl);
		if (absDFH instanceof FTPFileStrategyHandler)
		{
			deleteHandlerObject.setType(IFileDeleteHandler.FTP_TYPE);
		} else if (absDFH instanceof DefaultFileStrategyHandler)
		{
			deleteHandlerObject.setType(IFileDeleteHandler.NORMAL_TYPE);
		}
		res = absDFH.delete(deleteHandlerObject);
		if (null != nextHandler)
		{
			res = loopDelete(nextHandler, articleId);
		}
		return res;

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
