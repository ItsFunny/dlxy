package com.dlxy.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.vo.UserRecordUrlVO;

public class PictureRecordDetailHandler extends AbstractRecordDetailHandler
{

	public PictureRecordDetailHandler(String level)
	{
		super(AbstractRecordDetailHandler.PICTURE);
	}

	@Override
	protected Map<String, Object> parse(UserRecordDTO userRecordDTO)
	{
		String[] detail = userRecordDTO.getRecordDetail().split(":");
		List<UserRecordUrlVO>urls=new LinkedList<UserRecordUrlVO>();
		String[] pictureIds = detail[2].split(",");
		detail[3]="文章id:"+detail[3];
		for (String string : pictureIds)
		{
			//这里是有问题的这样写,不会正则
//			string.replaceAll(",", "");
			string = string.replaceAll("%", ":");
//			String url=File.separator+"imgs"+File.separator+articleId+File.separator+string;
			UserRecordUrlVO vo=new UserRecordUrlVO("图片", string);
			urls.add(vo);
		}
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("description", detail[0]);
		map.put("others", getOthers(detail));
		map.put("urls",urls);
		return map;
	}

}
