package com.dlxy.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.common.dto.AbstractDlxyTreeComposite;
import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.common.enums.DlxyTitleEnum;
import com.dlxy.config.DlxyObservervable;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.service.ITitleWrappedService;

public class TitleWrappedServiceImpl extends DlxyObservervable implements ITitleWrappedService
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
	public DlxyTitleDTO findDlxyDetailTitles(Integer limitNumber) throws SQLException
	{
		Collection<DlxyTitleDTO> collection = titleService.findTitlesByType(DlxyTitleEnum.NEWS_TITLE.ordinal());
		if (collection == null || collection.isEmpty())
		{
			return null;
		}
		DlxyTitleDTO dlxyTitleDTO = collection.iterator().next();
		// List<AbstractDlxyTitleComposite> l=new
		// ArrayList<>(titleService.findChildsByParentId(dlxyTitleDTO.getTitleId()));
		dlxyTitleDTO = findChildsAndArticles(dlxyTitleDTO.getTitleId(), limitNumber);
		// dlxyTitleDTO.setChildTitles(childs);
		return dlxyTitleDTO;
	}

	@Override
	public DlxyTitleDTO findChildsAndArticles(Integer titleId, int limitNumber) throws SQLException
	{

		DlxyTitleDTO dlxyTitleDTO = titleService.findById(titleId);
		if (dlxyTitleDTO == null)
		{
			return null;
		}
		// 找寻旗下的所有子类
		Collection<DlxyTitleDTO> childs = titleService.findChildsByParentId(titleId);
		if (null == childs || childs.isEmpty())
		{
			return dlxyTitleDTO;
		}
		// 如果需要显示父类的,在这里设置
		// if(null==childs || childs.isEmpty())
		// {
		// childs=new ArrayList<DlxyTitleDTO>();
		// }
		// childs.add(dlxyTitleDTO);

		// 找寻子类下的文章
		List<Integer> ids = new LinkedList<Integer>();
		// for (DlxyTitleDTO dlxyTitleDTO2 : childs)
		// {
		// dlxyTitleDTO.addChild(dlxyTitleDTO2);
		// }
		// childs.forEach(p->{
		// ids.add(p.getTitleId());
		// dlxyTitleDTO.addChild(p);
		// });
		for (DlxyTitleDTO d : childs)
		{
			ids.add(d.getTitleId());
		}
		dlxyTitleDTO.setChilds((List<? extends AbstractDlxyTreeComposite>) childs);
		List<ArticleDTO> articles = (List<ArticleDTO>) articleService.findArticlesInTitleIdsTopNumber(ids, limitNumber,
				ArticleStatusEnum.UP.ordinal());
		if (null == articles || articles.isEmpty())
		{
			return dlxyTitleDTO;
		}
		for (DlxyTitleDTO dlxyTitleDTO2 : childs)
		{
			for (int i = articles.size() - 1; i >= 0; i--)
			{
				Integer titleId2 = dlxyTitleDTO2.getTitleId();
				Integer titleId3 = articles.get(i).getTitleId();
				if (titleId3.equals(titleId2))
				{
					dlxyTitleDTO2.addArticle(articles.get(i));
					articles.remove(i);
				}
				// if(dlxyTitleDTO2.getTitleParentId().equals(articles.get(i).getTitleId()));
				// {
				// 这是一个bug吗,,无论如何都相等
				// dlxyTitleDTO2.addArticle(articles.get(i));
				// articles.remove(i);
				// }
			}
		}
		// childs.forEach(d->{
		// for(int i=articles.size()-1;i>=0;i--)
		// {
		// if(d.getTitleId().equals(articles.get(i).getTitleId()));
		// {
		// d.addArticle(articles.get(i));
		// articles.remove(i);
		// }
		// }
		// });
		return dlxyTitleDTO;
	}

}
