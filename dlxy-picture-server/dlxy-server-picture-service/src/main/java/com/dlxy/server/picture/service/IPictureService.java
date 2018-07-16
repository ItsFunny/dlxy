/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午8:57:37
* 
*/
package com.dlxy.server.picture.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.dlxy.common.dto.PictureDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月1日 下午8:57:37
*/
public interface IPictureService
{
	//不一定有用
	Collection<PictureDTO>findByArticleIdArray(Long[] articleIds);
	
	void addPicture(PictureDTO[] pictureDTOs) throws SQLException;
	/**
	 * 用于删除文章时,自动将其对应的图片状态更新为0,batch system 会自动清理对应的图片
	 * @param articleIds 传入的文章id ,是一个数组
	 * @param status	要更新的状态
	 * @author joker 
	 * @date 创建时间：2018年7月9日 上午9:33:23
	 */
	void updateArticlePictureStatusByArticleIdsInbatch(Long[] articleIds,int status);
	
	
	/**
	 * 通常用于上传文章时,将对应的图片状态修改为有效,核心:articleId,pictureIds
	 * @param status 要更新的状态
	 * @param articleId	文章id
	 * @param ids	要更新的具体图片id
	 * @throws SQLException
	 * @author joker 
	 * @date 创建时间：2018年7月9日 上午9:35:30
	 */
	void updateArticlePictureStatusByArticleIdLimited(int status,Long articleId,String[] ids) throws SQLException;
	
	void addPictureWithArticleId(PictureDTO[] pictureDTOs) throws SQLException;
	
	Collection<PictureDTO>findByStatus(int status);
	
	//无用的方法
//	int deleteByPictureIdAndStatus(String pictureId,int pictureType);
	
	int deleteByPictureIds(List<Long> pictureIds);
	
}
