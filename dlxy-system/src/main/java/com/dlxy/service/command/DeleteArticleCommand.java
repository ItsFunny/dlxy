package com.dlxy.service.command;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.utils.FileUtil;

public class DeleteArticleCommand extends Command
{

	private Logger logger = LoggerFactory.getLogger(DeleteArticleCommand.class);

	@Transactional
	@Override
	public void execute(Map<String, Object> p)
	{
		@SuppressWarnings("unchecked")
		List<Long> ids = (List<Long>) p.get("articleIdList");
		List<Long> backUpdateIds = new ArrayList<>();
		List<Long> deletedArticleIdlist = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		// articleGroup.delete(params);

		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String realPath = request.getServletContext().getRealPath("imgs");
		for (int i = ids.size() - 1; i >= 0; i--)
		{
			long artileId = ids.get(i);
			File file = new File(realPath + File.separator + artileId);
			try
			{
				// 如果这样做的话,下面就无法得到具体的pictureId了,如果想得到具体的id,则需要double for loop
				// 如果改为那种方式的话记得pictureGroup的delete也需要更改
				boolean isOk = FileUtil.delFileOrDir(file);
				if (isOk)
				{
					deletedArticleIdlist.add(artileId);
				} else
				{
					backUpdateIds.add(artileId);
				}
			} catch (Exception e)
			{
				logger.error("[删除文章旗下的图片失败],file:{}", file);
				backUpdateIds.add(ids.get(i));
			}
		}
		if (!deletedArticleIdlist.isEmpty())
		{
			params.clear();
			params.put("articleIdList", deletedArticleIdlist);
			articleGroup.delete(params);
			pictureGroup.delete(params);
		}
		if (!backUpdateIds.isEmpty())
		{
			params.clear();
			try
			{
				// 说明有部分文章下的图片删除不成功,也就意味着需要将这些文章下的图片状态全修改为无效
				params.put("type", "article");
				params.put("articleIdList", backUpdateIds);
				params.put("pictureStatus", PictureStatusEnum.Invalid.ordinal());
				pictureGroup.update(params);
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Autowired
	@Override
	protected void setArticleGroup(ArticleReceiver articleGroup)
	{
		// TODO Auto-generated method stub
		super.setArticleGroup(articleGroup);
	}

	@Autowired
	@Override
	protected void setPictureGroup(PictureReceiver pictureGroup)
	{
		// TODO Auto-generated method stub
		super.setPictureGroup(pictureGroup);
	}

	@Autowired
	@Override
	protected void setUserArticleGroup(UserArticleReceiver userArticleGroup)
	{
		// TODO Auto-generated method stub
		super.setUserArticleGroup(userArticleGroup);
	}

}
