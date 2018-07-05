/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午3:23:24
* 
*/
package com.dlxy.server.article.service;

import java.util.Collection;

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
	Collection<DlxyTitleDTO>findChildsByParentId(int titleParentId);
	
	Collection<DlxyTitleDTO>findAllParent();
}
