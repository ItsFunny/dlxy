package com.dlxy.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.dlxy.common.dto.PictureDTO;
import com.dlxy.common.dto.UserDTO;

public interface IPictureWrappedService
{
	Collection<Long> addPciture(UserDTO userDTO, Long articleId, PictureDTO[] pictureDTOs) throws SQLException;

	void updateDescPicture(UserDTO userDTO, Long articleId, PictureDTO pictureDTO) throws SQLException;

	void deletePicture(UserDTO userDTO, List<Long> pictureIds, int type);
}
