/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月8日 上午8:29:52
* 
*/
package com.dlxy.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.common.dto.AbstractDlxyTreeComponent;
import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.enums.DlxyTitleEnum;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.service.ITitleManagementWrappedService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月8日 上午8:29:52
*/
public class ManagemeentTitleServiceImpl extends Observable implements ITitleManagementWrappedService
{
	@Autowired
	private ITitleService titleService;
	@Autowired
	private IArticleService articleService;

	@Override
	public void addParent(DlxyTitleDTO dlxyTitleDTO)
	{
		
	}

	@Override
	public DlxyTitleDTO findDlxyDetailTitles()
	{
		Collection<DlxyTitleDTO> collection = titleService.findTitlesByType(DlxyTitleEnum.NEWS_TITLE.ordinal());
		DlxyTitleDTO dlxyTitleDTO=null;
		if(null!=collection && !collection.isEmpty())
		{
			dlxyTitleDTO=collection.iterator().next();
		}
		Collection<DlxyTitleDTO> childs =  titleService.findChildsByParentId(dlxyTitleDTO.getTitleId());
//		dlxyTitleDTO.setChildTitles(childs);
		List<AbstractDlxyTreeComponent>l=new ArrayList<>(childs);
		dlxyTitleDTO.setChilds(l);
		return dlxyTitleDTO;
	}

	@Override
	public DlxyTitleDTO findChildsAndArticles(Integer titleId,int limitNumber) throws SQLException
	{
		
		DlxyTitleDTO dlxyTitleDTO = titleService.findById(titleId);
		if(dlxyTitleDTO==null)
		{
			return null;
		}
		Collection<DlxyTitleDTO> childs = titleService.findChildsByParentId(titleId);
	
		if(null==childs || childs.isEmpty())
		{
			return dlxyTitleDTO;
		}
		List<Integer>ids=new LinkedList<Integer>();
		childs.forEach(p->{
			ids.add(p.getTitleId());
			dlxyTitleDTO.addChild(p);
		});
		List<ArticleDTO> articles = (List<ArticleDTO>) articleService.findArticlesInTitleIdsTopNumber(ids, limitNumber);
		if(null==articles || articles.isEmpty())
		{
			return dlxyTitleDTO;
		}
		for (DlxyTitleDTO dlxyTitleDTO2 : childs)
		{
			for(int i=articles.size()-1;i>=0;i--)
			{
				if(dlxyTitleDTO2.getTitleId().equals(articles.get(i).getTitleId()));
				{
					dlxyTitleDTO2.addArticle(articles.get(i));
					articles.remove(i);
				}
			}
		}
//		childs.forEach(d->{
//			for(int i=articles.size()-1;i>=0;i--)
//			{
//				if(d.getTitleId().equals(articles.get(i).getTitleId()));
//				{
//					d.addArticle(articles.get(i));
//					articles.remove(i);
//				}
//			}
//		});
		return dlxyTitleDTO;
	}

}
