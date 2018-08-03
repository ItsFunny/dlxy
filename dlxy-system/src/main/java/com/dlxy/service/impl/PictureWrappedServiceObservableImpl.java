package com.dlxy.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.dlxy.common.dto.PictureDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.enums.ArticlePictureTypeEnum;
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.config.DlxyObservervable;
import com.dlxy.server.picture.service.IPictureService;
import com.dlxy.service.AbstractRecordDetailHandler;
import com.dlxy.service.IPictureWrappedService;

public class PictureWrappedServiceObservableImpl extends DlxyObservervable implements IPictureWrappedService
{

	@Autowired
	private IPictureService pictureService;

	@Transactional
	@Override
	public Collection<Long> addPciture(UserDTO userDTO, Long articleId, PictureDTO[] pictureDTOs) throws SQLException
	{
		pictureService.addPicture(pictureDTOs);
		List<Long> idList = new ArrayList<Long>();
		Stream.of(pictureDTOs).forEach(p -> {
			idList.add(p.getPictureId());
		});
		pictureService.addPictureWithArticleId(pictureDTOs);
		setChanged();
		StringBuilder sb = new StringBuilder();
		for (PictureDTO pictureDTO : pictureDTOs)
		{
			pictureDTO.setArticleId(articleId);
			sb.append(pictureDTO.getPictureUrl().replaceAll(":", "%") + ",");
		}
		UserRecordDTO userRecordDTO = UserRecordDTO.getUserRecordDTO(userDTO.getUserId(),
				"添加图片:" + AbstractRecordDetailHandler.PICTURE + ":" + sb + ":" + articleId);
		notifyObservers(userRecordDTO);
		return idList;
	}

	@Override
	public void deletePicture(UserDTO userDTO, List<Long> pictureIds, int type)
	{
		int count = pictureService.deleteByPictureIds(pictureIds);
		if (count < 1)
		{
			throw new RuntimeException("wrong argument");
		}
		StringBuilder detail = null;
		if (type == ArticlePictureTypeEnum.DETAIL_PICTURE.ordinal())
		{
			detail = new StringBuilder("删除图片:picture:");
		} else
		{
			detail = new StringBuilder("删除图片文章图片:picture:");
		}
		for (Long long1 : pictureIds)
		{
			detail.append(long1 + ",");
		}
		// String detail="delete:portal-picture:"+pictureId;
		UserRecordDTO userRecordDTO = UserRecordDTO.getUserRecordDTO(userDTO.getUserId(), detail.toString());
		setChanged();
		notifyObservers(userRecordDTO);
	}

	@Transactional
	@Override
	public void updateDescPicture(UserDTO userDTO, Long articleId, PictureDTO pictureDTO) throws SQLException
	{
		List<Long> ids = new ArrayList<>();
		ids.add(articleId);
		pictureService.updateDescPicStatus(articleId, PictureStatusEnum.Invalid.ordinal());
		addPciture(userDTO, articleId, new PictureDTO[]
		{ pictureDTO });
	}

}
