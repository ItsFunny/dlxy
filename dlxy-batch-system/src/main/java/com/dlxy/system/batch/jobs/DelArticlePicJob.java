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
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.system.batch.service.IFileDeleteHandler;
import com.dlxy.system.batch.service.IFileDeleteHandler.DeleteHandlerObject;

/**
 * 自动删除那些添加了,但是不保存的图片
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
	private Logger logger = LoggerFactory.getLogger(DelArticlePicJob.class);

	@Autowired
	private QueryRunner queryRunner;

	@Autowired
	private IFileDeleteHandler fileDeleteHandler;

//	@Autowired
//	private FileStrategyContext fileStrategyContext;

	// 每15天的24:00点执行一次
	 @Scheduled(cron = "0 0 0 1/15 1-12 ? ")
//	@Scheduled(cron = "0/10 * * * * ? ")
	@Override
	public void run()
	{
		logger.info("[DelInvalidPics]begin the job ");
		Long beginTime = System.currentTimeMillis();
		String sql = "SELECT a.picture_id AS pictureId,b.picture_url AS pictureUrl,a.article_id AS articleId FROM dlxy_article_picture a LEFT JOIN dlxy_picture b ON a.picture_id=b.picture_id WHERE a.picture_status=?";
		Integer count = 0;
		try
		{
			List<Map<String, Object>> list = queryRunner.query(sql, new MapListHandler(),
					PictureStatusEnum.Invalid.ordinal());
			List<Long> deletedPictureIdList = new ArrayList<Long>();
			try
			{
				Integer size = list.size() - 1;
				for (int i = size; i >= 0; i--)
				{
					Map<String, Object> map = list.get(i);
					String url = (String) map.get("pictureUrl");
					Long pictureId = Long.parseLong(map.get("pictureId").toString());
//					如果指定存储方式的话只需要打开这段注释和上面需要的bean既可  ,不过需要重写下storeBaseUrl这块,因为存放的是其他服务器中的
//					String fifeUrl = fileStrategyContext.getStoreBasePath(IFileStrategy.IMG_TYPE) + File.separator
//							+ url.substring(url.indexOf(fileStrategyContext.getVisitPrefix(IFileStrategy.IMG_TYPE)));
//					fileStrategyContext.delete(fifeUrl);

					DeleteHandlerObject obj = new IFileDeleteHandler.DeleteHandlerObject();
					obj.setDbUrl(url);
					if (url.contains("http"))
					{
						obj.setType(IFileDeleteHandler.FTP_TYPE);
					} else
					{
						obj.setType(IFileDeleteHandler.NORMAL_TYPE);
					}
					Boolean isSuccess = fileDeleteHandler.delete(obj);
					if (isSuccess)
					{
						count++;
						deletedPictureIdList.add(pictureId);
						list.remove(i);
						logger.info("[删除图片]sucess,pictureId:{}", pictureId);
					} else
					{
						logger.error("[删除图片发生错误] 无法删除图片,错误未知,可能是文件不存在或者无权限,图片编号:{}", pictureId);
					}
				}
				if (!list.isEmpty())
				{
					StringBuilder sb = new StringBuilder();
					for (Map<String, Object> map : list)
					{
						sb.append(map.get("pictureId") + ",");
					}
					logger.error("[删除无效图片]some pics cant delete ,pictureIds detail:{}" + sb);
				}
				if (!deletedPictureIdList.isEmpty())
				{
					Integer dbCount = deleteRecordInDb(deletedPictureIdList);
					logger.info("[删除数据库中图片记录]delete {} records ", dbCount);
				}
			} catch (IllegalStateException | SecurityException e)
			{
				logger.error("[删除图片出现系统错误]可能是权限问题,error : {}", e.getMessage(), e);
			}
			deletedPictureIdList = null;
			list = null;
		} catch (SQLException e)
		{
			logger.error("[查询下线图片时候发生sql错误] error:{} cause:{}", e.getMessage(), e.getCause());
		}
		logger.info("[DelArtucleInvalidPicJob] finish the job,consume {} reords,consume {} millseconds", count,
				System.currentTimeMillis() - beginTime);
	}
	// @Scheduled(cron = "0 0 0 1/15 1-12 ? ")
	// @Override
	// public void run()
	// {
	// logger.info("[DelInvalidPics]begin the job ");
	// Long beginTime = System.currentTimeMillis();
	// String sql = "SELECT a.picture_id AS pictureId,b.picture_url AS
	// pictureUrl,a.article_id AS articleId FROM dlxy_article_picture a LEFT JOIN
	// dlxy_picture b ON a.picture_id=b.picture_id WHERE a.picture_status=?";
	// Integer count = 0;
	// try
	// {
	// List<Map<String, Object>> list = queryRunner.query(sql, new MapListHandler(),
	// PictureStatusEnum.Invalid.ordinal());
	// List<Long> pictureIdList = new ArrayList<Long>();
	//
	// String storeUrl = getStoreUrl();
	// if (StringUtils.isEmpty(storeUrl))
	// {
	// logger.error("[DeleteInvalidPics] finished unexpectedlly ,error:{}");
	// return;
	// }
	// try
	// {
	// Integer size = list.size() - 1;
	// String baseStoreUrl=null;
	// String visitPrefix=null;
	// String fileUrl=null;
	// for (int i = size; i >= 0; i--)
	// {
	// Map<String, Object> map = list.get(i);
	// String url = (String) map.get("pictureUrl");
	//
	// fileDeleteHandler.delete(url);
	// if (url.contains("http") || url.contains("https"))
	// {
	// // 说明图片是存储在其他服务器上的
	// baseStoreUrl =
	// configuration.getByKey(BatchSystemConfiguration.FTP_IMG_STORE);
	// visitPrefix =
	// configuration.getByKey(BatchSystemConfiguration.FTP_IMG_VISIT_PREFIX);
	// if (StringUtils.isEmpty(baseStoreUrl) || StringUtils.isEmpty(visitPrefix))
	// {
	// logger.error(
	// "[DeleteInvalidPics] init parameter values dont have default value ,job
	// finished upexceptedly");
	// return;
	// }
	// fileUrl = baseStoreUrl + url.substring(url.indexOf(visitPrefix));
	// Boolean delete = ftpFileStrategy.delete(fileUrl);
	// } else
	// {
	// baseStoreUrl =
	// configuration.getByKey(BatchSystemConfiguration.LOCAL_IMG_STORE);
	// if (StringUtils.isEmpty(baseStoreUrl))
	// {
	// baseStoreUrl = getStoreUrl();
	// configuration.addKeyValue(BatchSystemConfiguration.LOCAL_IMG_STORE,
	// baseStoreUrl);
	// }
	// visitPrefix=configuration.getByKey(BatchSystemConfiguration.LOCAL_IMG_VISIT_PREFIX);
	// if (StringUtils.isEmpty(baseStoreUrl) || StringUtils.isEmpty(visitPrefix))
	// {
	// logger.error(
	// "[DeleteInvalidPics] init parameter values for localVisitPrefix cant be
	// empty");
	// return;
	// }
	// fileUrl = baseStoreUrl + url.substring(url.indexOf(visitPrefix));
	// defaultFileStrategy.delete(fileUrl);
	//
	// }
	// fileUrl = storeUrl + url.substring(url.indexOf("/imgs"));
	// File file = new File(fileUrl);
	// if (file.exists())
	// {
	// boolean delete = file.delete();
	// Long pictureId = Long.parseLong(map.get("pictureId").toString());
	// if (delete)
	// {
	// count++;
	// pictureIdList.add(pictureId);
	// list.remove(i);
	// logger.info("[删除图片]sucess,pictureId:{}", pictureId);
	// } else
	// {
	// logger.error("[删除图片发生错误],错误未知,图片编号:{}", pictureId);
	// }
	// } else
	// {
	// Long pictureId = Long.parseLong(map.get("pictureId").toString());
	// pictureIdList.add(pictureId);
	// list.remove(i);
	// }
	// }
	// if (!list.isEmpty())
	// {
	// StringBuilder sb = new StringBuilder();
	// for (Map<String, Object> map : list)
	// {
	// sb.append(map.get("pictureId") + ",");
	// }
	// logger.error("[删除无效图片]some pics cant delete ,pictureIds detail:{}" + sb);
	// }
	// if (!pictureIdList.isEmpty())
	// {
	// Integer dbCount = deleteRecordInDb(pictureIdList);
	// logger.info("[删除数据库中图片记录]delete {} records ", dbCount);
	// }
	// } catch (IllegalStateException | SecurityException e)
	// {
	// logger.error("[删除图片出现系统错误]可能是权限问题,error : {},cause:{}", e.getMessage(),
	// e.getCause());
	// }
	// pictureIdList = null;
	// list = null;
	// } catch (SQLException e)
	// {
	// logger.error("[查询下线图片时候发生sql错误] error:{} cause:{}", e.getMessage(),
	// e.getCause());
	// }
	// logger.info("[DelArtucleInvalidPicJob] finish the job,consume {}
	// reords,consume {} millseconds", count,
	// System.currentTimeMillis() - beginTime);
	// }
	// public static void main(String[] args)
	// {
	// String url =
	// "http://localhost:8000/imgs/380076021405319168/608f4422-eb47-46bd-a605-f8de9de9005c.png";
	// System.out.println();
	// }

	// protected void process(String url)
	// {
	//
	// }

	private Integer deleteRecordInDb(List<Long> pictureIdList) throws SQLException
	{
		// 这点几w的数据并不会全表扫描,并且id同意为long类型
		StringBuilder sb = new StringBuilder("delete a from dlxy_picture a  where 1=1 ");
		sb.append(" and a.picture_id in ( ");
		sb.append(StringUtils.repeat("?,", pictureIdList.size() - 1));
		sb.append(" ? )");
		int count = queryRunner.update(sb.toString(), pictureIdList.toArray());

		return count;
	}

}
