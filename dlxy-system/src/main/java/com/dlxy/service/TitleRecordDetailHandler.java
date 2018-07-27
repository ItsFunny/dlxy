/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月24日 下午2:03:25
* 
*/
package com.dlxy.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.vo.UserRecordUrlVO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月24日 下午2:03:25
*/
public class TitleRecordDetailHandler extends AbstractRecordDetailHandler
{

	public TitleRecordDetailHandler(String level)
	{
		super(AbstractRecordDetailHandler.TITLE);
	}

	@Override
	protected Map<String, Object> parse(UserRecordDTO userRecordDTO)
	{
		Map<String, Object>map=new HashMap<String, Object>();
		String[] detail = userRecordDTO.getRecordDetail().split(":");
		String[] titleIds = detail[2].split(",");
		List<UserRecordUrlVO>urls=new LinkedList<UserRecordUrlVO>();
		for (String string : titleIds)
		{
			String url="/admin/title/article/"+string+".html";
			UserRecordUrlVO vo=new UserRecordUrlVO(string, url);
			urls.add(vo);
		}
		map.put("description", detail[0]);
		map.put("urls",urls);
		map.put("others", getOthers(detail));
		return map;
	}

}
