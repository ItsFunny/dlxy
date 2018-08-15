package com.dlxy.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.SuspicionDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.utils.PageResultUtil;
import com.dlxy.config.DlxyObservervable;
import com.dlxy.exception.DlxySuspicionException;
import com.dlxy.exception.DlxySystemIllegalException;
import com.dlxy.server.user.model.DlxyLink;
import com.dlxy.server.user.model.DlxyUser;
import com.dlxy.server.user.service.ILinkService;
import com.dlxy.server.user.service.IUserArticleService;
import com.dlxy.server.user.service.IUserRecordService;
import com.dlxy.server.user.service.IUserService;
import com.dlxy.service.AbstractRecordDetailHandler;
import com.dlxy.service.IUserWrappedService;
import com.dlxy.service.UserRecordHandlerFactory;
import com.joker.library.utils.KeyUtils;

public class UserWrappedServiceImpl extends DlxyObservervable implements IUserWrappedService
{
	
	
	@Autowired
	private IUserRecordService userRecordService;

	@Autowired
	private IUserArticleService userArticleService;

	@Autowired
	private IUserService userService;
	
	@Autowired
	private ILinkService linkService;

	@Override
	public PageDTO<Collection<Map<String, Object>>> findUserArticlesByPage(int pageSize, int pageNum,
			Map<String, Object> p) throws SQLException
	{
		Long count = userArticleService.countByParam(p);
		if (count >= 1)
		{
			Collection<Map<String, Object>> collection = userArticleService.findByPage(pageSize, pageNum, p);
			PageDTO<Collection<Map<String, Object>>> pageDTO = new PageDTO<>(count, collection);
			return pageDTO;
		}
		return PageResultUtil.emptyPage();
	}

	@Override
	public PageDTO<Collection<UserRecordDTO>> findUserRecords(int pageSize, int pageNum, Map<String, Object> params)
			throws SQLException
	{
		Long count = userRecordService.countRecords(params);
		if (count < 1)
		{
			return PageResultUtil.emptyPage();
		}
		Collection<UserRecordDTO> collection = userRecordService.findRecordByPage(pageSize, pageNum, params);
		AbstractRecordDetailHandler handler = UserRecordHandlerFactory.create();
		collection.forEach(r -> {
			handler.handler(r);
		});
		PageDTO<Collection<UserRecordDTO>> pageDTO = new PageDTO<Collection<UserRecordDTO>>(count, collection);
		return pageDTO;
	}

	@Override
	public PageDTO<Collection<UserDTO>> findUsersByPage(int pageSize, int pageNum, Map<String, Object> params)
			throws SQLException
	{
		Long count = userService.countUsersByParam(params);
		if (count < 1)
		{
			return PageResultUtil.emptyPage();
		}
		if (pageSize < 1)
		{
			pageSize = 10;
		}
		if (pageNum <= 0)
		{
			pageNum = 1;
		}
		Collection<UserDTO> users = userService.findUsersByPage((pageNum - 1) * pageSize, pageSize, params);
		PageDTO<Collection<UserDTO>> p = new PageDTO<Collection<UserDTO>>(count, users);
		return p;
	}

	@Override
	public void addUser(UserDTO loginUser, UserDTO userDTO) throws SQLException
	{
		String detail = "";
		Long dbUserId = userService.addUser(userDTO);
		detail = "添加用户:" + AbstractRecordDetailHandler.USER + ":" + dbUserId;
		setChanged();
		UserRecordDTO userRecordDTO = UserRecordDTO.getUserRecordDTO(loginUser.getUserId(), detail);
		notifyObservers(userRecordDTO);
	}

	@Override
	public void updateUserStatusByUserId(UserDTO loginUser, Long userId, int status)
	{
		userService.updateUserStatusByUserId(userId, status);
		String detail = "更新用户状态|update:" + AbstractRecordDetailHandler.USER + ":" + userId;
		setChanged();
		UserRecordDTO recordDTO = UserRecordDTO.getUserRecordDTO(loginUser.getUserId(), detail);
		notifyObservers(recordDTO);
	}

	@Override
	public String updateUser(UserDTO loginUser, DlxyUser objUser)
	{
		String error = null;
		UserDTO prevUser = userService.findByUsername(objUser.getRealname());
		if (prevUser != null && !prevUser.getUserId().equals(objUser.getUserId()))
		{
			// 这里似乎得记录消息
			error = "名字重复,请重新输入";
			return error;
		}
		if (!StringUtils.isEmpty(objUser.getPassword()))
		{
			objUser.setPassword(KeyUtils.md5Encrypt(objUser.getPassword()));
		}else {
			objUser.setPassword(null);
		}
		int count = userService.updateUserByUserId(objUser);
		if (count <= 0)
		{
			error = "用户不存在,请刷新重试";
		} else
		{
			String detail = "更新用户信息:" + AbstractRecordDetailHandler.USER + ":" + objUser.getUserId();
			setChanged();
			notifyObservers(UserRecordDTO.getUserRecordDTO(loginUser.getUserId(), detail));
		}
		return error;
	}

	@Override
	public Integer deleteUser(UserDTO loginUser, List<Long> userIds)
	{
		Integer d = userService.deleteUser(userIds);
		if (d > 0)
		{
			setChanged();
			StringBuffer sb = new StringBuffer();
			sb.append("删除用户:" + AbstractRecordDetailHandler.USER + ":");
			for (Long long1 : userIds)
			{
				sb.append(long1 + ",");
			}
			notifyObservers(UserRecordDTO.getUserRecordDTO(loginUser.getUserId(), sb.toString()));
		}
		return d;
	}

	@Override
	public Integer deleteUserSingle(UserDTO loginUser, Long userId)
	{
		Integer d = userService.deleteUseByUserId(userId);
		if (d > 0)
		{
			setChanged();
			StringBuffer sb = new StringBuffer();
			sb.append("删除用户:" + AbstractRecordDetailHandler.USER + ":" + userId);
			notifyObservers(UserRecordDTO.getUserRecordDTO(loginUser.getUserId(), sb.toString()));
		}
		return d;
	}

	@Override
	public void addLink(UserDTO loginUser, DlxyLink link)
	{
		String linkUrl=link.getLinkUrl();
		if (linkUrl.contains(":"))
		{
			linkUrl = linkUrl.replaceAll(":", "`");
		}
		Integer c = linkService.addOrUpdate(link);
		if(c>0)
		{
			setChanged();
			String detail="更新或者添加超链接:"+AbstractRecordDetailHandler.LINK+":"+linkUrl;
			notifyObservers(UserRecordDTO.getUserRecordDTO(loginUser.getUserId(), detail));
		}
	}

	@Override
	public void deleteLinkByLinkId(UserDTO loginUser, DlxyLink link)
	{
		Integer c = linkService.deleteByLinkId(link.getLinkId());
		if(c>0)
		{
			setChanged();
			String detail="删除超链接:"+AbstractRecordDetailHandler.LINK+":"+link.getLinkUrl()+":删除了名为:"+link.getLinkName()+"的超链接";
			notifyObservers(UserRecordDTO.getUserRecordDTO(loginUser.getUserId(), detail));
		}
	}
}
