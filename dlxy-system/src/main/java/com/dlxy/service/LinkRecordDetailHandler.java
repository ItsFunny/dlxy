/**
*
* @author joker 
* @date 创建时间：2018年8月13日 下午12:55:32
* 
*/
package com.dlxy.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.vo.UserRecordUrlVO;

public class LinkRecordDetailHandler extends AbstractRecordDetailHandler
{

	public LinkRecordDetailHandler(String level)
	{
		super(level);
	}

	@Override
	protected Map<String, Object> parse(UserRecordDTO userRecordDTO)
	{
		Map<String, Object>map=new HashMap<String, Object>();
		String[] detail = userRecordDTO.getRecordDetail().split(":");
		String[] urls = detail[2].split(",");
		List<UserRecordUrlVO>u=new LinkedList<UserRecordUrlVO>();
		for (String string : urls)
		{
			string=string.replaceAll("`", ":");
			UserRecordUrlVO vo=new UserRecordUrlVO("url", string);
			u.add(vo);
		}
		map.put("description", detail[0]);
		map.put("urls",u);
		map.put("others", getOthers(detail));
		return map;
	}

}
