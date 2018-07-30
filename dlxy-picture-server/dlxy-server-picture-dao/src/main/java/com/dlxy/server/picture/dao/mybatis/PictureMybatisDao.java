/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午8:46:39
* 
*/
package com.dlxy.server.picture.dao.mybatis;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.dlxy.common.dto.PictureDTO;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月1日 下午8:46:39
 */
@Mapper
public interface PictureMybatisDao
{
	// @Insert("insert into dlxy_picture
	// (picture_id,picture_url,picture_type,picture_status) values
	// (#{pictureId},#{pictureUrl},#{pictureType},#{pictureStatus}))")
	// void addPicture(PictureDTO pictureDTO);

	// @Update("update dlxy_article_picture set picture_status =#{status} where
	// article_id=#{articleId}")
	// void updatePicStatusByArticleId(@Param("articleId") Long articleId,
	// @Param("status") int status);

	void updatePicStatusByArticleIdInBatch(@Param("ids") Long[] articleIds, @Param("status") int status);

	void addPicturesInBatch(List<PictureDTO> pictureDTOs);

	void addPicture(PictureDTO pictureDTO);

	void addPictureWithAricleId(List<PictureDTO> pictureDTOs);

	void addPictureWithArticleIdSingle(PictureDTO pictureDTO);

	void updatePictureStausInBatch(@Param("status") int status, @Param("articleId") Long articleId,
			@Param("ids") String[] pictureIds);

	// 无用的方法
	@Delete("delete from dlxy_picture where picture_id=#{pictureId} and picture_type=#{pictureType}")
	int deleteByPictureId(@Param("pictureId") String pictureId, @Param("pictureType") Integer pictureType);

	int deleteByPictureIdsInBatch(List<Long> pictureIds);

	@Deprecated
	@Select("select picture_id,picture_url,create_date,picture_display_seq from dlxy_picture where picture_type=#{status} order by picture_display_seq desc")
	Collection<PictureDTO> findByStatus(int status);

	Collection<PictureDTO> findByArticleIdArray(Long[] articleIds);
}
