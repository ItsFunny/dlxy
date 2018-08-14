package com.dlxy.service;

import org.springframework.cache.annotation.CacheEvict;

public class CacheRefreshUtil
{
	@CacheEvict(cacheNames = "single_title", key = "#obj")
	public void clearSingle(Object obj)
	{
	}

	@CacheEvict(cacheNames = "titles", allEntries = true)
	public void clearTitles()
	{
	}
}
