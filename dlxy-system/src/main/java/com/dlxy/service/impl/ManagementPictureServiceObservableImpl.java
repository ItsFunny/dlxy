/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 下午1:04:20
* 
*/
package com.dlxy.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.dlxy.common.dto.PictureDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.enums.PictureTypeEnum;
import com.dlxy.server.picture.service.IPictureService;
import com.dlxy.service.IPictureManagementWrappedService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月2日 下午1:04:20
*/
public class ManagementPictureServiceObservableImpl extends Observable implements IPictureManagementWrappedService
{

	@Autowired
	private IPictureService pictureService;
	
	@Transactional
	@Override
	public Collection<Long> addPciture(Long userId,Long articleId, PictureDTO[] pictureDTOs) throws SQLException
	{
		pictureService.addPicture(pictureDTOs);
		List<Long>idList=new ArrayList<Long>();
		Stream.of(pictureDTOs).forEach(p->{
			idList.add(p.getPictureId());
		});
		pictureService.addPictureWithArticleId(pictureDTOs);
		setChanged();
		String string ="";
		for (PictureDTO pictureDTO : pictureDTOs)
		{
			pictureDTO.setArticleId(articleId);
			string+=pictureDTO.getPictureId();
		}
		UserRecordDTO userRecordDTO=UserRecordDTO.getUserRecordDTO(userId, "add:picture:"+string);
		notifyObservers(userRecordDTO);
		return idList;
	}

	@Override
	public void deletePicture(Long userId, List<Long> pictureIds,int type)
	{
//		int count = pictureService.deleteByPictureIdAndStatus(pictureId, pictureType);
		int count=pictureService.deleteByPictureIds(pictureIds);
		if(count<1)
		{
			throw new RuntimeException("wrong argument");
		}
		StringBuilder detail=null;
		if(type==PictureTypeEnum.Portal_Carousel.ordinal())
		{
			detail=new StringBuilder("delete:portal-picture:");
		}else {
			detail=new StringBuilder("delete:normal-picture:");
		}
		for (Long long1 : pictureIds)
		{
			detail.append(long1+",");
		}
//		String detail="delete:portal-picture:"+pictureId;
		UserRecordDTO userRecordDTO = UserRecordDTO.getUserRecordDTO(userId, detail.toString());
		setChanged();
		notifyObservers(userRecordDTO);
	}

	@Override
	public void addPortalPicture(Long userId, PictureDTO pictureDTO) throws SQLException
	{
		pictureService.addPicture(new PictureDTO[] {pictureDTO});
		setChanged();
		String detail="add:portal-picture:"+pictureDTO.getPictureId();
		UserRecordDTO recordDTO=UserRecordDTO.getUserRecordDTO(userId, detail);
		notifyObservers(recordDTO);
	}

}
