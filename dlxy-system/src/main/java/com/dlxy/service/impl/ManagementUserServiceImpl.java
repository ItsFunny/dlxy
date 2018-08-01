/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 下午5:21:08
* 
*/
package com.dlxy.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.utils.PageResultUtil;
import com.dlxy.server.user.model.DlxyUser;
import com.dlxy.server.user.service.IUserArticleService;
import com.dlxy.server.user.service.IUserRecordService;
import com.dlxy.server.user.service.IUserService;
import com.dlxy.service.AbstractRecordDetailHandler;
import com.dlxy.service.IUserMangementWrappedService;
import com.dlxy.service.UserRecordHandlerFactory;
import com.joker.library.utils.KeyUtils;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月2日 下午5:21:08
 */
public class ManagementUserServiceImpl extends Observable implements IUserMangementWrappedService
{
	@Autowired
	private IUserRecordService userRecordService;

	@Autowired
	private IUserArticleService userArticleService;
	
	@Autowired
	private IUserService userService;
	
	@Override
	public PageDTO<Collection<Map<String, Object>>> findUserArticlesByPage(int pageSize, int pageNum, Map<String, Object> p) throws SQLException
	{
//		if(p.containsKey("searchParam"))
//		{
//			try
//			{
//				Long userId=Long.parseLong(p.get("searchParam").toString());
//				p.put("userId", userId);
//			} catch (NumberFormatException e)
//			{
//				p.put("username", p.get("searchParam"));
//			}
//		}
		Long count = userArticleService.countByParam(p);
		if (count >= 1)
		{
//			Collection<Map<String, Object>> collection = userRecordService.findByPage(pageSize, pageNum, p);
//			PageDTO<Collection<Map<String, Object>>> pageDTO = new PageDTO<Collection<Map<String, Object>>>(count,
//					collection);
//			return pageDTO;
		
			Collection<Map<String, Object>> collection = userArticleService.findByPage(pageSize, pageNum, p);
			PageDTO<Collection<Map<String, Object>>>pageDTO=new PageDTO<>(count, collection);
			return pageDTO;
		}
		return PageResultUtil.emptyPage();
	}
	@Override
	public PageDTO<Collection<UserRecordDTO>> findUserRecords(int pageSize, int pageNum, Map<String, Object> params)
			throws SQLException
	{
		Long count = userRecordService.countRecords(params);
		if(count<1)
		{
			return PageResultUtil.emptyPage();
		}
		Collection<UserRecordDTO> collection = userRecordService.findRecordByPage(pageSize, pageNum, params);
		AbstractRecordDetailHandler handler = UserRecordHandlerFactory.create();
		collection.forEach(r->{
			handler.handler(r);
		});
		PageDTO<Collection<UserRecordDTO>>pageDTO=new PageDTO<Collection<UserRecordDTO>>(count, collection);
		return pageDTO;
	}
	@Override
	public PageDTO<Collection<UserDTO>> findUsersByPage(int pageSize, int pageNum, Map<String, Object> params)
			throws SQLException
	{
		Long count = userService.countUsersByParam(params);
		if(count<1)
		{
			return PageResultUtil.emptyPage();
		}
		if(pageSize<1)
		{
			pageSize=1;
		}
		if(pageNum<=0)
		{
			pageNum=1;
		}
		Collection<UserDTO> users = userService.findUsersByPage((pageNum-1)*pageSize, pageSize, params);
		PageDTO<Collection<UserDTO>>p=new PageDTO<Collection<UserDTO>>(count, users);
		return p;
	}
	
	@Override
	public void addUser(UserDTO loginUser, UserDTO userDTO) throws SQLException
	{
		String detail="";
		Long dbUserId = userService.addUser(userDTO);
		detail="添加用户:"+AbstractRecordDetailHandler.USER+":"+dbUserId;
		setChanged();
		UserRecordDTO userRecordDTO=UserRecordDTO.getUserRecordDTO(loginUser.getUserId(), detail);
		notifyObservers(userRecordDTO);
	}
	@Override
	public void updateUserStatusByUserId(UserDTO loginUser, Long userId,int status)
	{
		userService.updateUserStatusByUserId(userId, status);
//		String detail="update:user:"+userId+"|用户"+loginUser.getRealname()+"修改了userId为"+userId+"的状态,更改为了:"+status;
		String detail="更新用户状态|update:"+AbstractRecordDetailHandler.USER+":"+userId;
		setChanged();
		UserRecordDTO recordDTO=UserRecordDTO.getUserRecordDTO(loginUser.getUserId(), detail);
		notifyObservers(recordDTO);
	}
	@Override
	public String updateUser(UserDTO loginUser, DlxyUser objUser)
	{
		String error=null;
		UserDTO prevUser = userService.findByUsername(objUser.getRealname());
		if (prevUser != null && !prevUser.getUserId().equals(objUser.getUserId()))
		{
			//这里似乎得记录消息
			error = "名字重复,请重新输入";
		} else if (!StringUtils.isEmpty(objUser.getPassword()))
		{
			objUser.setPassword(KeyUtils.md5Encrypt(objUser.getPassword()));
		} else
		{
			int count = userService.updateUserByUserId(objUser);
			if (count <= 0)
			{
				error = "用户不存在,请刷新重试";
			}else {
				String detail="更新用户信息:"+AbstractRecordDetailHandler.USER+":"+objUser.getUserId();
				setChanged();
				notifyObservers(UserRecordDTO.getUserRecordDTO(loginUser.getUserId(), detail));
			}
		}
		
		return error;
	}
	@Override
	public Integer deleteUser(UserDTO loginUser, List<Long> userIds)
	{
		Integer d = userService.deleteUser(userIds);
		if(d>0)
		{
			setChanged();
			StringBuffer sb=new StringBuffer();
			sb.append("删除用户:"+AbstractRecordDetailHandler.USER+":");
			for (Long long1 : userIds)
			{
				sb.append(long1+",");
			}
			notifyObservers(UserRecordDTO.getUserRecordDTO(loginUser.getUserId(), sb.toString()));
		}
		return d;
	}
	@Override
	public Integer deleteUserSingle(UserDTO loginUser, Long userId)
	{
		Integer d=userService.deleteUseByUserId(userId);
		if(d>0)
		{
			setChanged();
			StringBuffer sb=new StringBuffer();
			sb.append("删除用户:"+AbstractRecordDetailHandler.USER+":"+userId);
			notifyObservers(UserRecordDTO.getUserRecordDTO(loginUser.getUserId(), sb.toString()));
		}
		return d;
	}

}
