/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午3:23:24
* 
*/
package com.dlxy.server.article.service;

import java.util.Collection;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月1日 下午3:23:24
*/
public interface ITitleService
{
	
	List<DlxyTitleDTO> findChildsByParentId(int titleParentId);
	
	DlxyTitleDTO findParentAndHisChilds(int titleParentId);
	
//	Collection<DlxyTitleDTO>findAllParent();
	
	List<DlxyTitleDTO>findTitlesByType(Integer type);
	
	
	//查找出真正新闻的页面
//	DlxyTitleDTO findNewsTitle();
	
	
	
	
	DlxyTitleDTO findById(int titleId);
	
	DlxyTitleDTO findByAbbName(String abbName);
	
	void insertOrUpdate(DlxyTitleDTO dlxyTitleDTO);
	
	Integer deleteByTitleId(Integer titleId);
}
