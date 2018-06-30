/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 上午8:49:22
* 
*/
package com.dlxy.system.management.controller;

import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.ResultDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.common.utils.ResultUtil;
import com.dlxy.common.vo.PageVO;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.system.management.service.IArticleFacadedService;
import com.dlxy.system.management.utils.ManagementUtil;
import com.joker.library.utils.CommonUtils;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月29日 上午8:49:22
 */
@RestController
@RequestMapping("/api")
public class RestAPIController
{
	private static Logger logger = LoggerFactory.getLogger(RestAPIController.class);

	@Autowired
	private IArticleFacadedService articleFacadedService;

	@Autowired
	private IArticleService articeService;

	// @RequestMapping("/article/del/{articleId}")
	// public ResultDTO<String> delArtilce(@PathVariable Long articleId)
	// {
	// UserDTO user = ManagementUtil.getLoginUser();
	// try
	// {
	// articleFacadedService.delArticle(user.getUserId(), articleId);
	// return ResultUtil.sucess();
	// } catch (Exception e)
	// {
	// e.printStackTrace();
	// return ResultUtil.fail("error:"+e.getMessage());
	// }
	// }
	@RequestMapping(value = "/article/del/batch")
	@ResponseBody
	public ResultDTO<String> delArticles(HttpServletRequest request, HttpServletResponse response)
	{
		UserDTO userDTO = ManagementUtil.getLoginUser();
		String ids = request.getParameter("ids");
		if (StringUtils.isEmpty(ids))
		{
			return ResultUtil.fail("missing argument");
		}
		String[] idArr = ids.split(",");
		try
		{
			Long[] idA = new Long[idArr.length];
			for (int i = 0; i < idArr.length; i++)
			{
				idA[i] = Long.valueOf(idArr[i]);
			}
			articleFacadedService.updateArticlesInBatch(userDTO.getUserId(), idA, ArticleStatusEnum.DELETE.ordinal());
			return ResultUtil.sucess();
		} catch (Exception e)
		{
			e.printStackTrace();
			return ResultUtil.fail("error:" + e.getMessage());
		}
	}

	@RequestMapping(value = "/article/del/batch2", method = RequestMethod.POST)
	@ResponseBody
	public ResultDTO<String> delArticles2(HttpServletRequest request, HttpServletResponse response)
	{
		System.out.println("coming in");
		return ResultUtil.sucess();
	}

	/*
	 * username,articleId,articleName,
	 */
	@RequestMapping("/search/article")
	public ResultDTO<PageVO<Collection<ArticleDTO>>> searchArticle(HttpServletRequest request,
			HttpServletResponse response)
	{
		String q = request.getParameter("q");
		String pageSizeStr = StringUtils.defaultString(request.getParameter("pageSize"), "2");
		String pageNumStr = StringUtils.defaultString(request.getParameter("pageNum"), "1");
		Integer pageSize = Integer.parseInt(pageSizeStr);
		Integer pageNum = Integer.parseInt(pageNumStr);
		if (StringUtils.isEmpty(q))
		{
			return ResultUtil.fail("参数不可为空");
		}
		String searchQuery = CommonUtils.validStringException(q.trim());
		try
		{
			Collection<ArticleDTO> findByArticleId = articeService.findByArticleId(Long.parseLong(searchQuery));
			return ResultUtil.sucess(new PageVO<Collection<ArticleDTO>>(findByArticleId, pageSize, pageNum, 1L));
		} catch (NumberFormatException e)
		{
			Map<String, Object> params = new HashMap<>();
			params.put("searchParam", searchQuery);
			try
			{
				PageDTO<Collection<ArticleDTO>> pageRes = articleFacadedService.serarchByParams((pageNum - 1) * pageSize,
						pageSize, params);
				return ResultUtil.sucess(new PageVO<Collection<ArticleDTO>>(pageRes.getData(), pageSize, pageNum,
						pageRes.getTotalCount()), "sucess");
			} catch (SQLException e1)
			{
				logger.error("[查询文章]发生sql错误");
				e1.printStackTrace();
				return ResultUtil.fail(e1.getMessage());
			}
		} catch (SQLException e)
		{
			logger.error("[查询文章]发生sql错误");
			e.printStackTrace();
			return ResultUtil.fail(e.getMessage());
		}
	}
	@RequestMapping("/search/article2")
	public ResultDTO<PageVO<Collection<ArticleDTO>>> searchArticle2(HttpServletRequest request,
			HttpServletResponse response)
	{
		System.out.println(request.getParameter("q"));
		return ResultUtil.sucess();
	}

}
