/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 下午1:04:20
* 
*/
package com.dlxy.system.management.service.impl;

import java.sql.SQLException;
import java.util.Observable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.dlxy.common.dto.PictureDTO;
import com.dlxy.common.dto.UserRecordDTO;
import com.dlxy.server.picture.service.IPictureService;
import com.dlxy.system.management.service.IPictureManagementWrappedService;

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
	public void addPciture(Long userId,Long articleId, PictureDTO[] pictureDTOs) throws SQLException
	{
		pictureService.addPicture(pictureDTOs);
		pictureService.addPictureWithArticleId(articleId, pictureDTOs);
		setChanged();
		String string ="";
		for (PictureDTO pictureDTO : pictureDTOs)
		{
			string+=pictureDTO.getPictureId();
		}
		UserRecordDTO userRecordDTO=UserRecordDTO.getUserRecordDTO(userId, "add:picture:"+string);
		notifyObservers(userRecordDTO);
	}

}
