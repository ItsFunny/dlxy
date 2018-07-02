/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 下午5:21:08
* 
*/
package com.dlxy.system.management.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.utils.PageResultUtil;
import com.dlxy.server.user.service.IUserRecordService;
import com.dlxy.system.management.service.IUserMangementWrappedService;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月2日 下午5:21:08
 */
@Service
public class ManagementUserServiceImpl implements IUserMangementWrappedService
{
	@Autowired
	private IUserRecordService userRecordService;

	@Override
	public PageDTO<Collection<Map<String, Object>>> findByPage(int pageSize, int pageNum, Map<String, Object> p) throws SQLException
	{
		Long count = userRecordService.countByParam(p);
		if (count >= 1)
		{
//			Collection<Map<String, Object>> collection = userRecordService.findByPage(pageSize, pageNum, p);
//			PageDTO<Collection<Map<String, Object>>> pageDTO = new PageDTO<Collection<Map<String, Object>>>(count,
//					collection);
//			return pageDTO;
			Collection<Map<String, Object>> collection = userRecordService.findByPage(pageSize, pageNum, p);
			PageDTO<Collection<Map<String, Object>>>pageDTO=new PageDTO<>(count, collection);
			return pageDTO;
		}
		return PageResultUtil.emptyPage();
	}

}
