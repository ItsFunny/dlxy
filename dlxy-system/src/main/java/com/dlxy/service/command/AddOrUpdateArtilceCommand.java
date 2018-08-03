package com.dlxy.service.command;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.service.AbstractRecordDetailHandler;

public class AddOrUpdateArtilceCommand extends Command
{

	@Transactional
	@Override
	public void execute(Map<String, Object> p)
	{
		String detail = null;
		Map<String, Object> params = new HashMap<String, Object>();
		if (!p.containsKey("articleDTO"))
		{
			throw new RuntimeException("missing argument articleDTO");
		}
		ArticleDTO a = (ArticleDTO) p.get("articleDTO");
		try
		{

			if (p.containsKey("update"))
			{
				this.articleGroup.update(p);
				detail = "更新文章:" + AbstractRecordDetailHandler.ARTICLE + ":" + a.getArticleId();
			} else
			{
				this.articleGroup.add(p);
				this.userArticleGroup.add(p);
				detail = "添加文章:" + AbstractRecordDetailHandler.ARTICLE + ":" + a.getArticleId();
			}
			if (p.containsKey("pictureStatus"))
			{
				params.clear();
				params.put("pictureIdList", p.get("pictureIdList"));
				params.put("type", "picture");
				params.put("pictureStatus", p.get("pictureStatus"));
				this.pictureGroup.update(params);
			}
			params = null;
			setChanged();
			UserRecordDTO userRecordDTO = UserRecordDTO.getUserRecordDTO(a.getUserId(), detail);
			notifyObservers(userRecordDTO);
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException("sql exception:" + e.getMessage());
		}
	}

	@Autowired
	@Override
	protected void setArticleGroup(ArticleReceiver articleGroup)
	{
		this.articleGroup = articleGroup;
	}

	@Autowired
	@Override
	protected void setPictureGroup(PictureReceiver pictureGroup)
	{
		this.pictureGroup = pictureGroup;
	}

	@Autowired
	@Override
	protected void setUserArticleGroup(UserArticleReceiver userArticleGroup)
	{
		this.userArticleGroup = userArticleGroup;
	}
}
