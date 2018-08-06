/**
*
* @author joker 
* @date 创建时间：2018年8月5日 下午5:13:33
* 
*/
package com.dlxy.service;

import org.springframework.cache.annotation.CacheEvict;

/**
 * 
 * @author joker
 * @date 创建时间：2018年8月5日 下午5:13:33
 */
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
