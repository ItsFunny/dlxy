/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 下午1:03:35
* 
*/
package com.dlxy.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.dlxy.common.dto.PictureDTO;
import com.dlxy.common.dto.UserDTO;

public interface IPictureWrappedService
{
	Collection<Long> addPciture(UserDTO userDTO,Long articleId,PictureDTO[] pictureDTOs) throws SQLException;
	
	void updateDescPicture(UserDTO userDTO,Long articleId,PictureDTO pictureDTO) throws SQLException;
	
	@Deprecated  //这个方法应该是会删除的
	Long addPictureWithArticleIdSingel(UserDTO userDTO,Long articleId,PictureDTO pictureDTO);
	
	
	@Deprecated//无效了,需求变了
	void addPortalPicture(UserDTO userDTO,PictureDTO pictureDTO) throws SQLException;
	
	 void deletePicture(UserDTO userDTO, List<Long> pictureIds,int type);
}
