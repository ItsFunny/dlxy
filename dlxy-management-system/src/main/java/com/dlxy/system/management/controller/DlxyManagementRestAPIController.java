/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 上午8:49:22
* 
*/
package com.dlxy.system.management.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dlxy.article.server.service.IArticleService;
import com.dlxy.common.dto.ResultDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月29日 上午8:49:22
*/
@Controller
public class DlxyManagementRestAPIController
{
	private static Logger logger=LoggerFactory.getLogger(DlxyManagementRestAPIController.class);
	@Autowired
	private IArticleService articleService;
	@RequestMapping("/article/del/{articleId}/{}")
	public ResultDTO<String> delArtilce(@PathVariable Long articleId)
	{
		try
		{
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return null;
		
		
	}
}
