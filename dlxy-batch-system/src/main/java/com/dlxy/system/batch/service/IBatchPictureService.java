package com.dlxy.system.batch.service;

import java.util.List;

import com.dlxy.server.picture.model.DlxyArticlePicture;

public interface IBatchPictureService
{
	List<DlxyArticlePicture>findByStatus(Integer status);
}
