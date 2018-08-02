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
	
	void addPicture(PictureDTO pictureDTO);
	/**
	 * 用于删除文章时,自动将其对应的图片状态更新为0,batch system 会自动清理对应的图片
	 * @param articleIds 传入的文章id ,是一个数组
	 * @param status	要更新的状态
	 * @author joker 
	 * @date 创建时间：2018年7月9日 上午9:33:23
	 */
	void updateArticlePictureStatusByArticleIdsInbatch(List<Long> articleIds,int status);
	
	/**
	 * 用于当更换轮播图的时候,修改原先的图片的状态为0,batch自动删除,当然也可以自己直接在这里删除了
	 * @param articleId
	 * @param status
	 * @return
	 * @author joker 
	 * @date 创建时间：2018年8月2日 下午12:10:07
	 */
	int updateDescPicStatus(Long articleId,Integer status);
	
	
	/**
	 * 通常用于上传文章时,将对应的图片状态修改为有效,核心:articleId,pictureIds
	 * @param status 要更新的状态
	 * @param articleId	文章id
	 * @param ids	要更新的具体图片id
	 * @throws SQLException
	 * @author joker 
	 * @date 创建时间：2018年7月9日 上午9:35:30
	 */
	@Deprecated
	void updateArticlePictureStatusByArticleIdLimited(int status,Long articleId,List<Long> pictureIds) ;
	
	Integer updatePictureStatusInPictureIds(Integer status,List<Long>pictureIdList);
	
	
	void addPictureWithArticleId(PictureDTO[] pictureDTOs) throws SQLException;
	
	void addPictureWithArticleIdSingle(PictureDTO pictureDTO);
	
	@Deprecated
	Collection<PictureDTO> findByStatus(int status);
	
	//无用的方法
//	int deleteByPictureIdAndStatus(String pictureId,int pictureType);
	
	int deleteByPictureIds(List<Long> pictureIds);
	
	
	/**
	 * 删除文章下的所有的图片记录
	 * @param articleIdList
	 * @return
	 * @author joker 
	 * @date 创建时间：2018年8月2日 下午1:15:50
	 */
	int deleteByArticleIdList(List<Long>articleIdList);
	
}
