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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.utils.PageResultUtil;
import com.dlxy.server.article.service.IArticleCountService;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.system.management.exception.ManagementIllegalException;
import com.dlxy.system.management.service.IArticleManagementWrappedService;
import com.joker.library.utils.CommonUtils;


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
	
	@Autowired
	private ITitleService titleService;

	@Override
	public PageDTO<Collection<ArticleDTO>> findByParams(int pageSize, int pageNum, Map<String, Object> params)
			throws SQLException
	{
		Long totalCount = articleCountService.countArticlesByDetailParam(params);
		if (totalCount <= 0)
		{
			return PageResultUtil.emptyPage();
		}
		if(pageSize<1)
		{
			pageSize=1;
		}
		if(pageNum<1)
		{
			pageNum=1;
		}
		if(params.containsKey("q"))
		{
			params.put("searchParam", params.get("q"));
		}
		Collection<ArticleDTO> datas = articleService.findByParam(params, (pageNum-1)*pageSize, pageSize);
		return new PageDTO<Collection<ArticleDTO>>(totalCount, datas);
	}


	@Override
	public void updateArticlesInBatch(Long userId,Long[] articleIds, int status)
	{
		articleService.updateArticleStatusInBatch(articleIds,status);
		StringBuilder sBuilder=new StringBuilder();
		for (Long long1 : articleIds)
		{
			sBuilder.append(long1+",");
		}
		execute(userId, "update:article:"+sBuilder+":status to "+status);
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
			String detail="rollback:article:"+articleId+":status to "+status;
			execute(userId, detail);
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


	@Override
	public PageDTO<Collection<ArticleDTO>> findAllArticles(int pageSize, int pageNum)
	{
		Long count = articleService.countAllArticles();
		if(count<0)
		{
			return PageResultUtil.emptyPage();
		}
		if(pageSize<1)
		{
			pageSize=1;
		}
		if(pageNum<1)
		{
			pageNum=1;
		}
		Collection<ArticleDTO> collection = articleService.findAllArticlesByPage((pageNum-1)*pageSize, pageSize);
		return new PageDTO<Collection<ArticleDTO>>(count, collection);
	}


	@Override
	public void addTitleOrUpdate(Long userId,DlxyTitleDTO dlxyTitleDTO)
	{
		titleService.insertOrUpdate(dlxyTitleDTO);
		execute(userId, "addOrUpdate:title:"+dlxyTitleDTO.getTitleId());
	}


	@Override
	public void deleteByTitleId(Long userId, Integer titleId)
	{
		DlxyTitleDTO dlxyTitleDTO = titleService.findById(titleId);
		if(null==dlxyTitleDTO)
		{
			ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			throw new ManagementIllegalException(CommonUtils.getRemortIP(request),userId,"试图删除不存在的标题:"+titleId);
		}
		titleService.deleteByTitleId(dlxyTitleDTO.getTitleId());
		String detail="delete:title:"+titleId+dlxyTitleDTO.getTitleName();
		execute(userId, detail);
	}


	@Override
	public void deleteInBatch(Long userId, Long[] articleIds)
	{
		articleService.deleteArticlesInBatch(articleIds);
		String detail="delete:article:";
		for (Long long1 : articleIds)
		{
			detail+=long1+",";
		}
		execute(userId, detail);
	}

	private void execute(Long userId,String detail)
	{
		setChanged();
		notifyObservers(UserRecordDTO.getUserRecordDTO(userId, detail));
	}
	

}
