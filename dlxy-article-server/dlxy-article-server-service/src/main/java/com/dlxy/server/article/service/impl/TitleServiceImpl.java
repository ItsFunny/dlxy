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
import com.dlxy.server.article.dao.mybatis.TitleDao;
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
	private TitleDao titleDao;

	@Override
	public Collection<DlxyTitleDTO> findByParentId(int titleParentId)
	{
		return titleDao.findAllParent(titleParentId);
	}

}
