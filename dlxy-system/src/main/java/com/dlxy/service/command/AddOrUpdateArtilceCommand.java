/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 上午11:55:43
* 
*/
package com.dlxy.service.command;

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
public class AddOrUpdateArtilceCommand extends Command
{

	
	@Transactional
	@Override
	public void execute(Map<String, Object> param)
	{
		String detail=null;
		if (!param.containsKey("articleDTO"))
		{
			throw new RuntimeException("missing argument articleDTO");
		}
		ArticleDTO a = (ArticleDTO) param.get("articleDTO");
		try
		{
			
			if(param.containsKey("update"))
			{
				this.articleGroup.update(param);
				detail="update:article:"+a.getArticleAuthor();
			}else {
				this.articleGroup.add(param);
				this.userArticleGroup.add(param);
				detail="add:article:"+a.getArticleId();
			}
			if(param.containsKey("pictureStatus"))
			{
				this.pictureGroup.update(param);
			}
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
