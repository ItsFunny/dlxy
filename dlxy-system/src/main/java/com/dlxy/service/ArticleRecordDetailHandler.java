package com.dlxy.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.vo.UserRecordUrlVO;

public class ArticleRecordDetailHandler extends AbstractRecordDetailHandler
{

	public ArticleRecordDetailHandler(String level)
	{
		super(level);
	}

	@Override
	protected Map<String, Object> parse(UserRecordDTO userRecordDTO)
	{
		String[] detail = userRecordDTO.getRecordDetail().split(":");
		Map<String, Object> map = new HashMap<String, Object>();
		String[] articleIds = detail[2].split(",");
		List<UserRecordUrlVO> urls = new LinkedList<>();
		for (String string : articleIds)
		{
			String url = "/article/detail/" + string + ".html";
			UserRecordUrlVO vo = new UserRecordUrlVO(string, url);
			urls.add(vo);
		}
		map.put("description", detail[0]);
		map.put("urls", urls);
		map.put("others", getOthers(detail));
		return map;
	}

}
