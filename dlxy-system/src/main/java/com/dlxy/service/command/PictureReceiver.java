package com.dlxy.service.command;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.common.dto.PictureDTO;
import com.dlxy.server.picture.service.IPictureService;

@SuppressWarnings("unchecked")
public class PictureReceiver implements IReceiver
{
	@Autowired
	private IPictureService pictureService;

	@Override
	public void add(Map<String, Object> map) throws SQLException
	{
		pictureService.addPicture(new PictureDTO[]
		{ (PictureDTO) map.get("pictureDTO") });
	}

	@Override
	public void update(Map<String, Object> params) throws SQLException
	{
		String updateBy = (String) params.get("type");
		Integer status = Integer.parseInt(params.get("pictureStatus").toString());
		if (updateBy.equals("article"))
		{
			List<Long> articleIdList = (List<Long>) params.get("articleIdList");
			pictureService.updateArticlePictureStatusByArticleIdsInbatch(articleIdList, status);
		} else if (updateBy.equals("picture"))
		{
			List<Long> pictureIdList = (List<Long>) params.get("pictureIdList");
			pictureService.updatePictureStatusInPictureIds(status, pictureIdList);
		}
	}

	@Override
	public void delete(Map<String, Object> params)
	{
		// String type=(String) params.get("type");
		//
		// if(type.equals("article"))
		// {
		//
		// }else if(type.equals("picture"))
		// {
		//
		// }
		List<Long> articleIdList = (List<Long>) params.get("articleIdList");
		pictureService.deleteByArticleIdList(articleIdList);
	}

}
