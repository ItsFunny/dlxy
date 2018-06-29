/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午6:58:57
* 
*/
package com.dlxy.system.management.controller;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.ResultDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.utils.ResultUtil;
import com.dlxy.common.vo.PageVO;
import com.dlxy.system.management.service.IArticleFacadedService;
import com.dlxy.system.management.utils.ManagementUtil;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月28日 下午6:58:57
 */
@Controller
@RequestMapping("/article")
public class ArticleController
{
	@Autowired
	@Qualifier("articleFacadedServiceImpl")
	private IArticleFacadedService articleFacadedService;

	@RequestMapping("/all")
	public ModelAndView showAll(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		Map<String, Object> params = new HashMap<String, Object>();
		String error=request.getParameter("error");
		String pageSizeStr = request.getParameter("pageSize");
		String pageNumStr = request.getParameter("pageNum");
		int pageSize = StringUtils.isEmpty(pageSizeStr) ? 1 : Integer.parseInt(pageSizeStr);
		int pageNum = StringUtils.isEmpty(pageNumStr) ? 1 : Integer.parseInt(pageNumStr);
		Map<String, String> p = new HashMap<>();
		p.put("articleIsRecommend", "0");
		PageDTO<Collection<ArticleDTO>> pageDTO = null;
		try
		{
			pageDTO = articleFacadedService.findArticles(pageSize, pageNum, p);
		} catch (SQLException e)
		{
			e.printStackTrace();
			params.put("error", e.getMessage());
		}
		if (params.containsKey("error"))
		{
			modelAndView = new ModelAndView("error", params);
			return modelAndView;
		}
		PageVO<Collection<ArticleDTO>> pageVO = new PageVO<Collection<ArticleDTO>>(pageDTO.getData(), pageSize, pageNum,
				pageDTO.getTotalCount());
		params.put("pageVO", pageVO);
		params.put("error", error);
		modelAndView = new ModelAndView("article-all", params);
		return modelAndView;
	}

	@RequestMapping("/del/{articleId}")
	public ModelAndView delArtilce(@PathVariable Long articleId)
	{
		ModelAndView modelAndView=null;
		Map<String, Object>params=new HashMap<>();
		UserDTO user = ManagementUtil.getLoginUser();
		try
		{
			articleFacadedService.delArticle(user.getUserId(), articleId);
			params.put("error", "删除成功");
			modelAndView=new ModelAndView("redirect:/article/all.html",params);
		} catch (Exception e)
		{
			e.printStackTrace();
			params.put("error", "删除失败");
			modelAndView=new ModelAndView("error",params);
		}
		return modelAndView;
	}

}
