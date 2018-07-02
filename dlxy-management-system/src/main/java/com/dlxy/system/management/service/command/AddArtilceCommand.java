/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 上午11:55:43
* 
*/
package com.dlxy.system.management.service.command;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.PictureDTO;
import com.dlxy.common.dto.UserRecordDTO;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月2日 上午11:55:43
 */
public class AddArtilceCommand extends Command
{

	
	@Transactional
	@Override
	public void execute(Map<String, Object> param)
	{
		if (!param.containsKey("articleDTO"))
		{
			throw new RuntimeException("missing argument articleDTO");
		}
		if (!param.containsKey("pictureDTO"))
		{
			throw new RuntimeException("missing argument pictureDTO");
		}
		this.articleGroup.add(param);
		this.userArticleGroup.add(param);
		try
		{
			this.pictureGroup.update(param);
			setChanged();
			ArticleDTO a = (ArticleDTO) param.get("articleDTO");
			UserRecordDTO userRecordDTO = UserRecordDTO.getUserRecordDTO(a.getUserId(), "add:article:"+a.getArticleId());
			notifyObservers(userRecordDTO);
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException("sql exception:" + e.getMessage());
		}
	}

	@Autowired
	@Override
	protected void setArticleGroup(ArticleGroup articleGroup)
	{
		this.articleGroup=articleGroup;
	}

	@Autowired
	@Override
	protected void setPictureGroup(PictureGroup pictureGroup)
	{
		this.pictureGroup=pictureGroup;
	}

	@Autowired
	@Override
	protected void setUserArticleGroup(UserArticleGroup userArticleGroup)
	{
		this.userArticleGroup=userArticleGroup;
	}
}
