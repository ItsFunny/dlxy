/**
*
* @author joker 
* @date 创建时间：2018年8月13日 上午11:17:53
* 
*/
package com.dlxy.server.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dlxy.server.user.dao.mybatis.DlxyLinkDao;
import com.dlxy.server.user.dao.mybatis.LinkDao;
import com.dlxy.server.user.model.DlxyLink;
import com.dlxy.server.user.model.DlxyLinkExample;
import com.dlxy.server.user.service.ILinkService;

/**
* 
* @author joker 
* @date 创建时间：2018年8月13日 上午11:17:53
*/
@Service
public class LinkServiceImpl implements ILinkService
{
	
	@Autowired
	private LinkDao linkDao;
	
	
	
	@Cacheable(cacheNames="links")
	@Override
	public List<DlxyLink> findAllLinks()
	{
		return linkDao.selectByExample(new DlxyLinkExample());
	}

	
	@CacheEvict(cacheNames="links",allEntries=true)
	@Override
	public Integer addOrUpdate(DlxyLink dlxyLink)
	{
		return linkDao.addOrUpdate(dlxyLink);
	}

	@CacheEvict(cacheNames="links",allEntries=true)
	@Override
	public Integer deleteByLinkId(Integer linkId)
	{
		return linkDao.deleteByPrimaryKey(linkId);
	}


	@Override
	public DlxyLink findById(Integer linkId)
	{
		return linkDao.selectByPrimaryKey(linkId);
	}

}
