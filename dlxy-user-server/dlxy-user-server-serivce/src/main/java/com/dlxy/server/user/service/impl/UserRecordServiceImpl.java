/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午7:04:32
* 
*/
package com.dlxy.server.user.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.server.user.dao.mybatis.UserRecordMybatisDao;
import com.dlxy.server.user.dao.query.UserRecordQueryDao;
import com.dlxy.server.user.service.IUserRecordService;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月29日 下午7:04:32
 */
@Service
public class UserRecordServiceImpl implements IUserRecordService
{

	@Autowired
	private UserRecordMybatisDao userRecordMybatisDao;

	@Autowired
	private UserRecordQueryDao userRecordQueryDao;

	public void addRecord(UserRecordDTO userRecordDTO)
	{
		userRecordMybatisDao.addRecord(userRecordDTO);
	}

	@Override
	public Collection<Map<String, Object>> findByPage(int pageSize, int pageNum, Map<String, Object> params)
			throws SQLException
	{
		if (pageNum <= 0)
		{
			pageNum = 1;
		}
		if (pageSize <= 0)
		{
			pageSize = 10;
		}
		params.put("start", (pageNum - 1) * pageSize);
		params.put("end", pageSize);
		return userRecordQueryDao.findByPage(params);
	}

	@Override
	public Long countByParam(Map<String, Object> params) throws SQLException
	{
		return userRecordQueryDao.countByParam(params);
	}

//	@Override
//	public Collection<ArticleDTO> findByPage(int pageSize, int pageNum, Map<String, Object> params) throws SQLException
//	{
//		 if(pageNum<=0)
//		{
//			pageNum = 1;
//		}
//		if (pageSize <= 0)
//		{
//			pageSize = 10;
//		}
//		params.put("start", (pageNum - 1) * pageSize);
//		params.put("end", pageSize);
//		return userRecordQueryDao.findByPage(params);
//	}

}
