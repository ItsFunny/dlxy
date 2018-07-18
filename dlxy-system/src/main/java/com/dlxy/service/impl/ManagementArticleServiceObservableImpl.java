/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 上午9:27:47
* 
*/
package com.dlxy.service.impl;


import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.PictureDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.common.event.AppEvent;
import com.dlxy.common.event.AppEventPublisher;
import com.dlxy.common.event.Events;
import com.dlxy.common.utils.PageResultUtil;
import com.dlxy.exception.DlxySystemIllegalException;
import com.dlxy.server.article.service.IArticleCountService;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.server.picture.service.IPictureService;
import com.dlxy.service.IArticleManagementWrappedService;
import com.dlxy.utils.FileUtil;
import com.joker.library.utils.CommonUtils;
import com.rabbitmq.client.Return;


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
	
	@Autowired
	private AppEventPublisher appEventPublisher;
	@Autowired
	private IPictureService pictureService;

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
	public void updateArticlesInBatch(UserDTO userDTO,Long[] articleIds, int status)
	{
		articleService.updateArticleStatusInBatch(articleIds,status);
		StringBuilder sBuilder=new StringBuilder();
		for (Long long1 : articleIds)
		{
			sBuilder.append(long1+",");
		}
		execute(userDTO, "update:article:"+sBuilder+":status to "+status);
	}
	@Override
	public void updateArticleTypeInBatch(UserDTO userDTO, Long[] articleIds, int type)
	{
			articleService.updateArticleTypeInbatch(articleIds, type);
			StringBuilder sBuilder=new StringBuilder();
			for (Long long1 : articleIds)
			{
				sBuilder.append(long1+",");
			}
			execute(userDTO, "update:article:"+sBuilder+":type to "+type);
	}

	@Override
	public ArticleDTO findByArticleId(Long articleId) throws SQLException
	{
		
		return articleService.findArticleDetailByArticleId(articleId);
	}


	@Override
	public int rollBackArticle(UserDTO userDTO,int status, Long articleId, int titleId)
	{
		int count=0;
		try
		{
			count = articleService.rollBackArticle(status, articleId, titleId);
			String detail="rollback:article:"+articleId+":status to "+status;
			execute(userDTO, detail);
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
	public void addTitleOrUpdate(UserDTO userDTO,DlxyTitleDTO dlxyTitleDTO)
	{
		titleService.insertOrUpdate(dlxyTitleDTO);
		execute(userDTO, "addOrUpdate:title:"+dlxyTitleDTO.getTitleId());
	}


	@Override
	public void deleteByTitleId(UserDTO userDTO, Integer titleId)
	{
		DlxyTitleDTO dlxyTitleDTO = titleService.findById(titleId);
		if(null==dlxyTitleDTO)
		{
			ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			IllegalLogDTO illegalLogDTO=new IllegalLogDTO(CommonUtils.getRemortIP(request), userDTO.getUserId(),"试图删除不存在的标题", IllegalLevelEnum.Suspicious.ordinal());
			throw new DlxySystemIllegalException(illegalLogDTO);
		}
		titleService.deleteByTitleId(dlxyTitleDTO.getTitleId());
		String detail="delete:title:"+titleId+dlxyTitleDTO.getTitleName();
		execute(userDTO, detail);
	}


	@Override
	public void deleteInBatch(UserDTO userDTO, Long[] articleIds)
	{
		articleService.deleteArticlesInBatch(articleIds);
		//修改图片状态,或者发布信息,最后还是选择只修改状态,防止瞬间进行io操作  2018-07-09 不需要了,直接数据库自动设置为null即可,因为url中存放着路径
		pictureService.updateArticlePictureStatusByArticleIdsInbatch(articleIds, PictureStatusEnum.Invalid.ordinal());
		//直接全部删除  采用发布消息的方式
		ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String realPath = request.getServletContext().getRealPath("imgs");
		for (Long long1 : articleIds)
		{
			System.out.println(realPath+File.separator+long1);
			File file=new File(realPath+File.separator+long1);
			FileUtil.delFileOrDir(file);
		}
//		Collection<PictureDTO> collection = pictureService.findByArticleIdArray(articleIds);
		String detail="delete:article:";
		for (Long long1 : articleIds)
		{
			detail+=long1+",";
		}
		execute(userDTO, detail);
	}
	

	private void execute(UserDTO userDTO,String detail)
	{
		setChanged();
		notifyObservers(UserRecordDTO.getUserRecordDTO(userDTO.getUserId(), detail));
	}


	@Override
	public PageDTO<Collection<ArticleDTO>> findByTitleIds(int pageSize, int pageNum, List<Integer> ids) throws SQLException
	{
		Long count = articleCountService.coutArtilcesByTitleIds(ids.toArray(new Integer[ids.size()]));
		if(count<=0)
		{
			return PageResultUtil.emptyPage();
		}else {
			if(pageSize<=0)
			{
				pageSize=5;
			}
			if(pageNum<=0)
			{
				pageNum=1;
			}
			Collection<ArticleDTO> collection2 = articleService.findArtilcesByTilteIdsAndPage(pageSize, pageNum, ids);
			return new PageDTO<Collection<ArticleDTO>>(count, collection2);
		}
	}


	

}
