/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午3:24:02
* 
*/
package com.dlxy.server.article.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.server.article.dao.mybatis.TitleMybatisDao;
import com.dlxy.server.article.model.DlxyTitleExample;
import com.dlxy.server.article.model.DlxyTitleExample.Criteria;
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
		DlxyTitleExample example=new DlxyTitleExample();
		Criteria criteria = example.createCriteria();
		criteria.andTitleParentIdEqualTo(titleParentId);
		List<DlxyTitleDTO> all=titleDao.selectByExample(example);
//		List<DlxyTitleDTO> all = (List<DlxyTitleDTO>) titleDao.findParentAllChilds(titleParentId);
//		DlxyTitleDTO dlxyTitleDTO=null;
//		if(all==null || all.isEmpty())
//		{
//			return null;
//		}
//		for(int i=all.size()-1;i>=0;i--)
//		{
//			if(all.get(i).getTitleId()==titleParentId)
//			{
//				dlxyTitleDTO=all.get(i);
//				all.remove(i);
//			}
//		}
//		dlxyTitleDTO.setChilds(all);
		return all;
	}
	@Override
	public DlxyTitleDTO findParentAndHisChilds(int titleParentId)
	{
		List<DlxyTitleDTO> all = (List<DlxyTitleDTO>) titleDao.findParentAndChildsWithUnion(titleParentId);
		DlxyTitleDTO dlxyTitleDTO=null;
		if(null==all || all.isEmpty())
		{
			return null;
		}
		for(int i=all.size()-1;i>=0;i--)
		{
			if(all.get(i).getTitleId()==titleParentId)
			{
				dlxyTitleDTO=all.get(i);
				all.remove(i);
				break;
			}
		}
		dlxyTitleDTO.setChilds(all);
		return dlxyTitleDTO;
	}

//	@Override
//	public Collection<DlxyTitleDTO> findAllParent()
//	{
//		return titleDao.findByType(DlxyTitleEnum.UP_TITLE.ordinal());
//	}

	@Override
	public DlxyTitleDTO findById(int titleId)
	{
		return titleDao.selectByPrimaryKey(titleId);
//		return titleDao.findById(titleId);
	}
	@Override
	public DlxyTitleDTO findByAbbName(String abbName)
	{
		DlxyTitleExample example=new DlxyTitleExample();
		Criteria criteria = example.createCriteria();
		criteria.andTitleAbbNameEqualTo(abbName);
		List<DlxyTitleDTO> list = titleDao.selectByExample(example);
		if(null==list || list.isEmpty())
		{
			return null;
		}else if(list.size()>1)
		{
			throw new RuntimeException("find multi titles");
		}else {
			 return list.iterator().next();
		}
	}


	@Override
	public void insertOrUpdate(DlxyTitleDTO dlxyTitleDTO)
	{
		titleDao.insertOrUpdate(dlxyTitleDTO);
	}

	@Override
	public Integer deleteByTitleId(Integer titleId)
	{
		DlxyTitleExample example=new DlxyTitleExample();
		Criteria criteria = example.createCriteria();
		criteria.andTitleIdNotEqualTo(0);
		criteria.andTitleIdEqualTo(titleId);
		int count = titleDao.deleteByExample(example);
		return count;
//		titleDao.deleteByTitleId(titleId);
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
		DlxyTitleExample example=new DlxyTitleExample();
		Criteria criteria = example.createCriteria();
		criteria.andTitleIdNotEqualTo(0);
		criteria.andTitleParentIdEqualTo(0);
		if(type>=0)
		{
			criteria.andTitleTypeEqualTo(type);
		}
		example.setOrderByClause("title_display_seq desc");
		return titleDao.selectByExample(example);
//		return titleDao.findByType(type);
	}
	
	

}
