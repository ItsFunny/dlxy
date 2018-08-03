package com.dlxy.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.vo.UserRecordUrlVO;

public class UserRecordDetailHandler extends AbstractRecordDetailHandler
{

	public UserRecordDetailHandler(String level)
	{
		super(level);
	}

	@Override
	protected Map<String, Object> parse(UserRecordDTO userRecordDTO)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		String[] detail = userRecordDTO.getRecordDetail().split(":");
		String userId = detail[2];
		String[] strings = userId.split(",");
		List<UserRecordUrlVO> urls = new LinkedList<UserRecordUrlVO>();
		for (String string : strings)
		{
			UserRecordUrlVO vo = new UserRecordUrlVO(string, "/admin/user/records.html?userId=" + string);
			urls.add(vo);
		}
		params.put("urls", urls);
		params.put("description", detail[0]);
		params.put("others", getOthers(detail));
		return params;
	}

}
