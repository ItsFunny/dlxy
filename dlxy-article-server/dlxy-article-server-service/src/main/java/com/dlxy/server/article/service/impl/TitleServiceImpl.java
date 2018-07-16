/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午3:24:02
* 
*/
package com.dlxy.server.article.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.enums.DlxyTitleEnum;
import com.dlxy.server.article.dao.mybatis.TitleMybatisDao;
import com.dlxy.server.article.service.ITitleService;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月1日 下午3:24:02
 */
@Service
public class TitleServiceImpl implements ITitleService
{
	@Autowired
	private TitleMybatisDao titleDao;

	@Override
	public Collection<DlxyTitleDTO> findChildsByParentId(int titleParentId)
	{
		return titleDao.findParentAllChilds(titleParentId);
	}

//	@Override
//	public Collection<DlxyTitleDTO> findAllParent()
//	{
//		return titleDao.findByType(DlxyTitleEnum.UP_TITLE.ordinal());
//	}

	@Override
	public DlxyTitleDTO findById(int titleId)
	{
		return titleDao.findById(titleId);
	}

	@Override
	public void insertOrUpdate(DlxyTitleDTO dlxyTitleDTO)
	{
		titleDao.insertOrUpdate(dlxyTitleDTO);
	}

	@Override
	public void deleteByTitleId(Integer titleId)
	{
		titleDao.deleteByTitleId(titleId);
	}

//	@Override
//	public DlxyTitleDTO findNewsTitle()
//	{
//		Collection<DlxyTitleDTO> collection = titleDao.findByType(DlxyTitleEnum.NEWS_TITLE.ordinal());
//		if(null!=collection && !collection.isEmpty())
//		{
//			return collection.iterator().next();
//		}else {
//			return null;
//		}
//	}

	@Override
	public Collection<DlxyTitleDTO> findTitlesByType(Integer type)
	{
		return titleDao.findByType(type);
	}

}
