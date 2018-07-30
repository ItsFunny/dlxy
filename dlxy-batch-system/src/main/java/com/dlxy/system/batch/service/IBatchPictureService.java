/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月30日 上午11:38:37
* 
*/
package com.dlxy.system.batch.service;

import java.util.List;

import com.dlxy.server.picture.model.DlxyArticlePicture;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月30日 上午11:38:37
*/
public interface IBatchPictureService
{
	List<DlxyArticlePicture>findByStatus(Integer status);
}
