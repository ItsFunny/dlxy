/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 上午11:40:57
* 
*/
package com.dlxy.service.command;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.PictureDTO;
import com.dlxy.server.picture.service.IPictureService;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月2日 上午11:40:57
 */
public class PictureGroup implements IGroup
{
	@Autowired
	private IPictureService pictureService;

	@Override
	public void add(Map<String, Object> map) throws SQLException
	{
		pictureService.addPicture(new PictureDTO[]
		{ (PictureDTO) map.get("pictureDTO") });
	}

	@Override
	public void update(Map<String, Object> params) throws SQLException
	{
//		ArticleDTO articleDTO = (ArticleDTO) params.get("articleDTO");
		List<Long>pictureIdList=(List<Long>) params.get("pictureIdList");
		Integer status = Integer.parseInt(params.get("pictureStatus").toString());
		// pictureService.updateArticlePictureStatus(pictureDTO.getArticleId(),
		// pictureDTO.getPictureStatus());
		

		pictureService.updatePictureStatusInPictureIds(status, pictureIdList);
//		pictureService.updateArticlePictureStatusByArticleIdLimited(Integer.parseInt(status.toString()),
//				articleDTO.getArticleId(), articleDTO.getPictureIds());
	}

	@Override
	public void delete(Map<String, Object> params)
	{
		Long[] pictureIds = (Long[]) params.get("pictureIds");
		pictureService.deleteByPictureIds(Arrays.asList(pictureIds));
	}

}
