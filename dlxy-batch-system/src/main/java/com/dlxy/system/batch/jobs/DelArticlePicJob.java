/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月9日 上午8:35:01
* 
*/
package com.dlxy.system.batch.jobs;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dlxy.common.dto.ResultDTO;
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.common.event.AppEventPublisher;
import com.dlxy.common.utils.FileUtil;
import com.dlxy.common.utils.RSAUtils;
import com.dlxy.system.batch.config.DlxyProperty;

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
	private Logger logger = LoggerFactory.getLogger(DelArticlePicJob.class);

	private String picStoreUrl;

	@Autowired
	private DlxyProperty dlxyProperty;
	@Autowired
	private QueryRunner queryRunner;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AppEventPublisher eventPublisher;

	@Scheduled(cron = "0/10 * * * * ?")
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
			List<Long> pictureIdList = new ArrayList<Long>();
//			List<Map<String, Object>> list = new LinkedList<>();
//			Map<String, Object> map2 = new HashMap<String, Object>();
//			map2.put("pictureUrl", "http://localhost:8000/imgs/7/608d59bc-290d-4691-9d46-602b2a77726a.JPG");
//			map2.put("pictureId", 29);
//			list.add(map2);
			if (StringUtils.isEmpty(picStoreUrl))
			{
				String token = RSAUtils.encryptByPublic("getImgsAddress", dlxyProperty.getPublicKeyBytes());
				@SuppressWarnings("rawtypes")
				ResultDTO resultDTO = restTemplate.getForObject(
						"http://localhost:8000/api/v1/address/images.html?token=" + URLEncoder.encode(token, "utf-8"),
						ResultDTO.class);
				if (resultDTO.getCode() == 1)
				{
					this.picStoreUrl = (String) resultDTO.getData();
				} else
				{
					logger.error("[rest请求存放图片地址出错]error:{}", resultDTO.getMsg());
					return;
				}
				try
				{
					Integer size=list.size()-1;
					for (int i = size; i >= 0; i--)
					{
						Map<String, Object> map = list.get(i);
						// }
						// for (Map<String, Object> map : list)
						// {
						String url = (String) map.get("pictureUrl");
						String fileUrl = picStoreUrl + url.substring(url.indexOf("/imgs"));
						File file = new File(fileUrl);
						if (file.exists())
						{
							boolean delete = file.delete();
							if (delete)
							{
								count++;
								Long pictureId = Long.parseLong(map.get("pictureId").toString());
								pictureIdList.add(pictureId);
								list.remove(i);
								logger.info("[删除图片]sucess,pictureId:{}",pictureId);
							} else
							{
								logger.error("[删除图片发生错误],错误未知");
							}
						}else {
							Long pictureId = Long.parseLong(map.get("pictureId").toString());
							pictureIdList.add(pictureId);
							list.remove(i);
						}
					}
					if(!list.isEmpty())
					{
						StringBuilder sb=new StringBuilder();
						for (Map<String, Object> map : list)
						{
							sb.append(map.get("pictureId")+",");
						}
						logger.error("[删除无效图片]some pics cant delete ,pictureIds detail:{}"+sb);
					}
					if(!pictureIdList.isEmpty())
					{
						Integer dbCount = deleteRecordInDb(pictureIdList);
						logger.info("[删除数据库中图片记录]delete {} records ",dbCount);
					}
				} catch (IllegalStateException | SecurityException e)
				{
					logger.error("[删除图片出现错误],error : {},cause:{}", e.getMessage(), e.getCause());
				}
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			logger.error("[查询下线图片时候发生sql错误] error:{} cause:{}", e.getMessage(), e.getCause());
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			logger.error("[删除无效图片]rsa加密的时候出错,error:{}", e.getMessage());
		}
		logger.info("[DelArtucleInvalidPicJob] finish the job,consume {} reords,consume {} millseconds", count,
				System.currentTimeMillis() - beginTime);
	}
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
