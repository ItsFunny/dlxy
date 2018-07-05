/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 上午9:27:47
* 
*/
package com.dlxy.system.management.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.utils.PageResultUtil;
import com.dlxy.server.article.service.IArticleCountService;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.system.management.service.IArticleManagementWrappedService;


/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月2日 上午9:27:47
*/
public class ManagementArticleServiceObservableImpl extends Observable implements IArticleManagementWrappedService
{
	
	@Autowired
	private IArticleService articleService;
	@Autowired
	private IArticleCountService articleCountService;
	

	@Override
	public PageDTO<Collection<ArticleDTO>> findByParams(int start, int end, Map<String, Object> params)
			throws SQLException
	{
		Long totalCount = articleCountService.countArticlesByDetailParam(params);
		if (totalCount <= 0)
		{
			return PageResultUtil.emptyPage();
		}
		Collection<ArticleDTO> datas = articleService.findByParam(params, start, end);
		return new PageDTO<Collection<ArticleDTO>>(totalCount, datas);
	}


	@Override
	public void updateArticlesInBatch(Long userId,Long[] articleIds, int status)
	{
		articleService.updateArticleStatusInBatch(articleIds,status);
		setChanged();
		StringBuilder sBuilder=new StringBuilder();
		for (Long long1 : articleIds)
		{
			sBuilder.append(long1+",");
		}
		UserRecordDTO userRecordDTO=UserRecordDTO.getUserRecordDTO(userId, "update:article:"+sBuilder+":status to "+status);
		notifyObservers(userRecordDTO);
	}

	@Override
	public ArticleDTO findByArticleId(Long articleId) throws SQLException
	{
		
		return articleService.findByArticleId(articleId);
	}


	@Override
	public int rollBackArticle(Long userId,int status, Long articleId, int titleId)
	{
		int count=0;
		try
		{
			count = articleService.rollBackArticle(status, articleId, titleId);
			setChanged();
			String detail="rollback:article:"+articleId+":status to "+status;
			notifyObservers(UserRecordDTO.getUserRecordDTO(userId,detail));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return count;
	}

	/*
	 * 强业务类型需要加上事务
	 */
	@Transactional
	@Override
	public void insertOrUpdate(ArticleDTO articleDTO)
	{
		/*
		 * 1.文章表插入数据
		 * 2.user_article 插入数据
		 * 3.修改图片状态
		 */
		articleService.insertOrUpdate(articleDTO);
		
	}


	@Override
	public void updateArticleByArticleId(ArticleDTO articleDTO) throws SQLException
	{
		articleService.update(articleDTO);
	}

	

}
