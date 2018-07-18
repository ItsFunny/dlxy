/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月8日 上午8:29:52
* 
*/
package com.dlxy.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Observable;

import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.enums.DlxyTitleEnum;
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
		List<DlxyTitleDTO> childs = (List<DlxyTitleDTO>) titleService.findChildsByParentId(dlxyTitleDTO.getTitleId());
		dlxyTitleDTO.setChildTitles(childs);
		return dlxyTitleDTO;
	}

}
