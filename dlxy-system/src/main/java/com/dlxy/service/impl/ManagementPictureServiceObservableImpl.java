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
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.common.enums.ArticlePictureTypeEnum;
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.server.picture.service.IPictureService;
import com.dlxy.service.AbstractRecordDetailHandler;
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
	public Collection<Long> addPciture(UserDTO userDTO,Long articleId, PictureDTO[] pictureDTOs) throws SQLException
	{
		pictureService.addPicture(pictureDTOs);
		List<Long>idList=new ArrayList<Long>();
		Stream.of(pictureDTOs).forEach(p->{
			idList.add(p.getPictureId());
		});
		pictureService.addPictureWithArticleId(pictureDTOs);
		setChanged();
		StringBuilder sb=new StringBuilder();
		for (PictureDTO pictureDTO : pictureDTOs)
		{
			pictureDTO.setArticleId(articleId);
//			String suffix = pictureDTO.getPictureUrl().substring(pictureDTO.getPictureUrl().lastIndexOf("."));
			sb.append(pictureDTO.getPictureUrl().replaceAll(":", "%")+",");
		}
		UserRecordDTO userRecordDTO=UserRecordDTO.getUserRecordDTO(userDTO.getUserId(), "添加图片:"+AbstractRecordDetailHandler.PICTURE+":"+sb+":"+articleId);
		notifyObservers(userRecordDTO);
		return idList;
	}
	@Transactional
	@Override
	public Long addPictureWithArticleIdSingel(UserDTO userDTO, Long articleId, PictureDTO pictureDTO)
	{
		pictureService.addPicture(pictureDTO);
		Long pictureId=pictureDTO.getPictureId();
		pictureService.addPictureWithArticleIdSingle(pictureDTO);
//		String detail="add:picture:"+pictureId;
		UserRecordDTO userRecordDTO=UserRecordDTO.getUserRecordDTO(userDTO.getUserId(), "添加图片:picture:"+pictureId);
		setChanged();
		notifyObservers(userRecordDTO);
		return pictureId;
	}

	@Override
	public void deletePicture(UserDTO userDTO, List<Long> pictureIds,int type)
	{
//		int count = pictureService.deleteByPictureIdAndStatus(pictureId, pictureType);
		int count=pictureService.deleteByPictureIds(pictureIds);
		if(count<1)
		{
			throw new RuntimeException("wrong argument");
		}
		StringBuilder detail=null;
		if(type==ArticlePictureTypeEnum.DETAIL_PICTURE.ordinal())
		{
			detail=new StringBuilder("删除图片:picture:");
		}else {
			detail=new StringBuilder("删除图片文章图片:picture:");
		}
		for (Long long1 : pictureIds)
		{
			detail.append(long1+",");
		}
//		String detail="delete:portal-picture:"+pictureId;
		UserRecordDTO userRecordDTO = UserRecordDTO.getUserRecordDTO(userDTO.getUserId(), detail.toString());
		setChanged();
		notifyObservers(userRecordDTO);
	}

	@Override
	public void addPortalPicture(UserDTO userDTO, PictureDTO pictureDTO) throws SQLException
	{
		pictureService.addPicture(new PictureDTO[] {pictureDTO});
		setChanged();
		String detail="添加首页文章图片:picture:"+pictureDTO.getPictureId();
		UserRecordDTO recordDTO=UserRecordDTO.getUserRecordDTO(userDTO.getUserId(), detail);
		notifyObservers(recordDTO);
	}
	@Transactional
	@Override
	public void updateDescPicture(UserDTO userDTO, Long articleId, PictureDTO pictureDTO) throws SQLException
	{
		//更新原先图片的状态
		pictureService.updateArticlePictureStatusByArticleIdsInbatch(new Long[] {articleId}, PictureStatusEnum.Invalid.ordinal());
		//查询新的图片
		addPciture(userDTO,articleId,new PictureDTO[] {pictureDTO});
	}
	


}
