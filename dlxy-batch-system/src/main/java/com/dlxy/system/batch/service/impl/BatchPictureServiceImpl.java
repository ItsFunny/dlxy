/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月30日 上午11:39:10
* 
*/
package com.dlxy.system.batch.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlxy.server.picture.dao.mybatis.DlxyArticlePictureDao;
import com.dlxy.server.picture.model.DlxyArticlePicture;
import com.dlxy.server.picture.model.DlxyArticlePictureExample;
import com.dlxy.server.picture.model.DlxyArticlePictureExample.Criteria;
import com.dlxy.system.batch.service.IBatchPictureService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月30日 上午11:39:10
*/
@Service
public class BatchPictureServiceImpl implements IBatchPictureService
{
	@Autowired
	private DlxyArticlePictureDao articlePictureDao;
	
	
	@Override
	public List<DlxyArticlePicture> findByStatus(Integer status)
	{
		DlxyArticlePictureExample example=new DlxyArticlePictureExample();
		Criteria criteria = example.createCriteria();
		criteria.andPictureStatusEqualTo(status);
		List<DlxyArticlePicture> pictures = articlePictureDao.selectByExample(example);
		return pictures;
	}

}
