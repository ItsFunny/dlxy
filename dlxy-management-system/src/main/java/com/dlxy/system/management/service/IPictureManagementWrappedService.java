/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 下午1:03:35
* 
*/
package com.dlxy.system.management.service;

import java.sql.SQLException;

import com.dlxy.common.dto.PictureDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月2日 下午1:03:35
*/
public interface IPictureManagementWrappedService
{
	void addPciture(Long userId,Long articleId,PictureDTO[] pictureDTOs) throws SQLException;
}
