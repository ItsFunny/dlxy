/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午9:47:18
* 
*/
package com.dlxy.server.picture.service.impl;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.common.dto.PictureDTO;
import com.dlxy.common.enums.ArticlePictureTypeEnum;
import com.dlxy.server.picture.dao.mybatis.DlxyArticlePictureDao;
import com.dlxy.server.picture.dao.mybatis.PictureMybatisDao;
import com.dlxy.server.picture.dao.query.PictureQueryDao;
import com.dlxy.server.picture.model.DlxyArticlePicture;
import com.dlxy.server.picture.model.DlxyArticlePictureExample;
import com.dlxy.server.picture.model.DlxyPictureExample;
import com.dlxy.server.picture.model.DlxyPictureExample.Criteria;
import com.dlxy.server.picture.service.IPictureService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月1日 下午9:47:18
*/
@Service
public class PictureServiceImpl implements IPictureService
{
	@Autowired
	private PictureQueryDao pictureQueryDao;

	@Autowired
	private PictureMybatisDao pictureMybatisDao;
	
	
	/*
	 * batch insert
	 */
	@Override
	public void addPicture(PictureDTO[] pictureDTOs) throws SQLException
	{
		pictureMybatisDao.addPicturesInBatch(Arrays.asList(pictureDTOs));
	}
	/*
	 * singel insert
	 */
	@Override
	public void addPicture(PictureDTO pictureDTO)
	{
		pictureMybatisDao.addPicture(pictureDTO);
	}

	@Override
	public void updateArticlePictureStatusByArticleIdsInbatch(List<Long> articleIds, int status)
	{
		pictureMybatisDao.updatePicStatusByArticleIdInBatch(articleIds, status);
	}

	@Override
	public void addPictureWithArticleId(PictureDTO[] pictureDTOs) throws SQLException
	{
		pictureMybatisDao.addPictureWithAricleId(Arrays.asList(pictureDTOs));
	}
	
	@Override
	public void addPictureWithArticleIdSingle(PictureDTO pictureDTO)
	{
		pictureMybatisDao.addPictureWithArticleIdSingle(pictureDTO);
	}

	@Override
	public void updateArticlePictureStatusByArticleIdLimited(int status,Long articleId,List<Long> ids) 
	{
		pictureMybatisDao.updatePictureStausInBatch(status, articleId, ids);
	}
	@Override
	public Integer updatePictureStatusInPictureIds(Integer status, List<Long> pictureIdList)
	{
		DlxyArticlePictureExample example=new DlxyArticlePictureExample();
		com.dlxy.server.picture.model.DlxyArticlePictureExample.Criteria criteria = example.createCriteria();
		criteria.andPictureIdIn(pictureIdList);
		DlxyArticlePicture picture=new DlxyArticlePicture();
		picture.setPictureStatus(status);
		return pictureMybatisDao.updateByExampleSelective(picture, example);
	}
	@Override
	public int updateDescPicStatus(Long articleId, Integer status)
	{
		DlxyArticlePictureExample example=new DlxyArticlePictureExample();
		com.dlxy.server.picture.model.DlxyArticlePictureExample.Criteria criteria = example.createCriteria();
		criteria.andArticleIdEqualTo(articleId);
		criteria.andPictureTypeEqualTo(ArticlePictureTypeEnum.DESCRIPTION_PICTURE.ordinal());
		DlxyArticlePicture articlePicture=new DlxyArticlePicture();
		articlePicture.setPictureStatus(status);
		return pictureMybatisDao.updateByExampleSelective(articlePicture, example);
	}

	@Override
	public Collection<PictureDTO> findByStatus(int status)
	{
		return pictureMybatisDao.findByStatus(status);
	}

//	@Override
//	public int deleteByPictureIdAndStatus(String pictureId, int pictureType)
//	{
//		return pictureMybatisDao.deleteByPictureId(pictureId, pictureType);
//	}

	@Override
	public int deleteByPictureIds(List<Long> pictureIds)
	{
		 return pictureMybatisDao.deleteByPictureIdsInBatch(pictureIds);
	}

	@Override
	public Collection<PictureDTO> findByArticleIdArray(Long[] articleIds)
	{
		return pictureMybatisDao.findByArticleIdArray(articleIds);
	}
	@Override
	public int deleteByArticleIdList(List<Long> articleIdList)
	{
		return pictureMybatisDao.deleteByArticleIdList(articleIdList);
	}
	

	
}
