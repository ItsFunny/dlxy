/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月31日 下午3:31:47
* 
*/
package com.dlxy.service.command;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.utils.FileUtil;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月31日 下午3:31:47
*/
public class DeleteArticleCommand extends Command
{

	private Logger logger=LoggerFactory.getLogger(DeleteArticleCommand.class);
	@Override
	public void execute(Map<String, Object> p)
	{
		@SuppressWarnings("unchecked")
		List<Long> ids = (List<Long>) p.get("ids");
		List<Long> backUpdateIds = new ArrayList<>();
		List<Long>deleteIds=new ArrayList<>();
		Map<String, Object>params=new HashMap<String, Object>();
		params.put("articleIds", ids);
		articleGroup.delete(params);
		
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String realPath = request.getServletContext().getRealPath("imgs");
		for (int i = ids.size() - 1; i >= 0; i--)
		{
			long artileId=ids.get(i);
			File file = new File(realPath + File.separator + artileId);
			try
			{
				boolean isOk = FileUtil.delFileOrDir(file);
				if(isOk)
				{
					deleteIds.add(artileId);
				}else {
					backUpdateIds.add(artileId);
				}
			} catch (Exception e)
			{
				logger.error("[删除文章旗下的图片失败],file:{}", file);
				backUpdateIds.add(ids.get(i));
			}
		}
		if(!deleteIds.isEmpty())
		{
			params.clear(); 
			params.put("pictureIds", deleteIds);
			pictureGroup.delete(params);
//			pictureService.deleteByPictureIds(deleteIds);
		}
		if (!backUpdateIds.isEmpty())
		{
			params.clear();
			try
			{
				params.put("pictureIdList", backUpdateIds);
				pictureGroup.update(params);
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
