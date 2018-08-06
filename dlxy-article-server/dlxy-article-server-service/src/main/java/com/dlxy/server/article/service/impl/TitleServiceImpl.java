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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.service.ITitleCacheServiec;
import com.dlxy.server.article.dao.mybatis.TitleMybatisDao;
import com.dlxy.server.article.model.DlxyTitleExample;
import com.dlxy.server.article.model.DlxyTitleExample.Criteria;
import com.dlxy.server.article.service.IBeanSelefAware;
import com.dlxy.server.article.service.ITitleService;

/**
 * 缓存只有single_title 和titles ,没有依据AbbName生成的cache,因为删除的时候是根据id删除的
 * 也没有child_titles,因为每次删除的时候也只是通过id,无法获取到到paretnId,除非删之前先查找
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月1日 下午3:24:02
 */
@Service
public class TitleServiceImpl implements ITitleService,IBeanSelefAware,ITitleCacheServiec
{
	@Autowired
	private TitleMybatisDao titleDao;

	private ITitleCacheServiec proxyTitleService;

	@Override
	public List<DlxyTitleDTO> findChildsByParentId(int titleParentId)
	{
		DlxyTitleExample example = new DlxyTitleExample();
		Criteria criteria = example.createCriteria();
		criteria.andTitleParentIdEqualTo(titleParentId);
		List<DlxyTitleDTO> all = titleDao.selectByExample(example);
		return all;
	}
	

	@Override
	public DlxyTitleDTO findParentAndHisChilds(int titleParentId)
	{
		DlxyTitleDTO dlxyTitleDTO = findById(titleParentId);
		List<DlxyTitleDTO> childs = findChildsByParentId(titleParentId);
		dlxyTitleDTO.setChilds(childs);
		// List<DlxyTitleDTO> all = (List<DlxyTitleDTO>)
		// titleDao.findParentAndChildsWithUnion(titleParentId);
		// DlxyTitleDTO dlxyTitleDTO = null;
		// if (null == all || all.isEmpty())
		// {
		// return null;
		// }
		// for (int i = all.size() - 1; i >= 0; i--)
		// {
		// if (all.get(i).getTitleId() == titleParentId)
		// {
		// dlxyTitleDTO = all.get(i);
		// all.remove(i);
		// break;
		// }
		// }
		// dlxyTitleDTO.setChilds(all);
		return dlxyTitleDTO;
	}

	// @Override
	// public Collection<DlxyTitleDTO> findAllParent()
	// {
	// return titleDao.findByType(DlxyTitleEnum.UP_TITLE.ordinal());
	// }

	@Cacheable(cacheNames = "single_title", key = "#titleId")
	@Override
	public DlxyTitleDTO findById(int titleId)
	{
		return titleDao.selectByPrimaryKey(titleId);
		// return titleDao.findById(titleId);
	}

	@Override
	public DlxyTitleDTO findByAbbName(String abbName)
	{
		DlxyTitleExample example = new DlxyTitleExample();
		Criteria criteria = example.createCriteria();
		criteria.andTitleAbbNameEqualTo(abbName);
		List<DlxyTitleDTO> list = titleDao.selectByExample(example);
		if (null == list || list.isEmpty())
		{
			return null;
		} else if (list.size() > 1)
		{
			throw new RuntimeException("find multi titles");
		} else
		{
			return list.iterator().next();
		}
	}

	@Override
	public void insertOrUpdate(DlxyTitleDTO dlxyTitleDTO)
	{
		if (dlxyTitleDTO.getTitleId() == null || dlxyTitleDTO.getTitleId().equals(0))
		{
//			dlxyCacheRefresh.clearTitles();
			this.proxyTitleService.clearTitles();
		}
		if (dlxyTitleDTO.getTitleId() != null)
		{
//			clearSingle(dlxyTitleDTO.getTitleId());
			this.proxyTitleService.clearSingle(dlxyTitleDTO.getTitleId());
		}
		titleDao.insertOrUpdate(dlxyTitleDTO);
	}
	@CacheEvict(cacheNames = "single_title", key = "#obj")
	@Override
	public void clearSingle(Object obj)
	{
	}

	@CacheEvict(cacheNames = "titles", allEntries = true)
	@Override
	public void clearTitles()
	{
	}


	@CacheEvict(cacheNames = "titles", allEntries = true)
	@Override
	public Integer deleteByTitleId(Integer obj)
	{
		DlxyTitleExample example = new DlxyTitleExample();
		Criteria criteria = example.createCriteria();
		criteria.andTitleIdNotEqualTo(0);
		criteria.andTitleIdEqualTo(obj);
		int count = titleDao.deleteByExample(example);
		return count;
		// titleDao.deleteByTitleId(titleId);
	}

	// @Override
	// public DlxyTitleDTO findNewsTitle()
	// {
	// Collection<DlxyTitleDTO> collection =
	// titleDao.findByType(DlxyTitleEnum.NEWS_TITLE.ordinal());
	// if(null!=collection && !collection.isEmpty())
	// {
	// return collection.iterator().next();
	// }else {
	// return null;
	// }
	// }

	@Cacheable(cacheNames = "titles", key = "#type")
	@Override
	public List<DlxyTitleDTO> findTitlesByType(Integer type)
	{
		DlxyTitleExample example = new DlxyTitleExample();
		Criteria criteria = example.createCriteria();
		criteria.andTitleIdNotEqualTo(0);
		criteria.andTitleParentIdEqualTo(0);
		if (type >= 0)
		{
			criteria.andTitleTypeEqualTo(type);
		}
		example.setOrderByClause("title_display_seq desc");
		return titleDao.selectByExample(example);
		// return titleDao.findByType(type);
	}

	@Override
	public void setSelf(Object proxy)
	{
		this.proxyTitleService= (ITitleCacheServiec) proxy;
	}

}
