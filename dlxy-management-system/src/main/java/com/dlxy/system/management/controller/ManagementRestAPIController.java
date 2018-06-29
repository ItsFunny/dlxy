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

import com.dlxy.common.dto.ResultDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.utils.ResultUtil;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.system.management.service.IArticleFacadedService;
import com.dlxy.system.management.utils.ManagementUtil;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月29日 上午8:49:22
*/
@Controller
public class ManagementRestAPIController
{
	private static Logger logger=LoggerFactory.getLogger(ManagementRestAPIController.class);
	
	@Autowired
	private IArticleFacadedService articleFacadedService;
	
	
//	@RequestMapping("/article/del/{articleId}")
//	public ResultDTO<String> delArtilce(@PathVariable Long articleId)
//	{
//		UserDTO user = ManagementUtil.getLoginUser();
//		try
//		{
//			articleFacadedService.delArticle(user.getUserId(), articleId);
//			return ResultUtil.sucess();
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//			return ResultUtil.fail("error:"+e.getMessage());
//		}
//	}
}
