/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月9日 下午7:11:21
* 
*/
package com.dlxy.system.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.ResultDTO;
import com.dlxy.common.utils.ResultUtil;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.system.model.ArticleVO;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月9日 下午7:11:21
 */
@RestController
@RequestMapping("/api")
public class RestAPIController
{
	@Autowired
	private ITitleService titleService;
	@Autowired
	private IArticleService articleService;

	/**
	 * @param parentId
	 *            父类的id
	 * @param request
	 * @param response
	 * @return 返回这个父类下的所有子类
	 * @author joker
	 * @date 创建时间：2018年7月7日 下午2:24:52
	 */
	@RequestMapping(value = "/title/{parentId}", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<Collection<DlxyTitleDTO>> findTitles(@PathVariable Integer parentId, HttpServletRequest request,
			HttpServletResponse response)
	{
		Collection<DlxyTitleDTO> collection = titleService.findChildsByParentId(parentId);
		if (null == collection || collection.isEmpty())
		{
			collection = titleService.findAllParent();
			return ResultUtil.needMoreOp(collection, "原类目不存在");
		}
		return ResultUtil.sucess(collection);
	}

	@RequestMapping(value = "/titles", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<Collection<DlxyTitleDTO>> findParentAllChilds(HttpServletRequest request,
			HttpServletResponse response)
	{
		try
		{
			Collection<DlxyTitleDTO> collection = titleService.findAllParent();
			return ResultUtil.sucess(collection);
		} catch (Exception e)
		{
			return ResultUtil.fail(e.getMessage());
		}
	}

	/*
	 * 所有的公告文章
	 */
	@RequestMapping(value = "/article/recommend", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResultDTO<Collection<ArticleVO>> findAllArticleRecommend(HttpServletRequest request,
			HttpServletResponse response)
	{
		Collection<ArticleDTO> articleDTOs = articleService.findAllRecommendArticles();
		List<ArticleVO>articleVOs=new ArrayList<ArticleVO>();
		for (ArticleDTO articleDTO : articleDTOs)
		{
			ArticleVO articleVO=new ArticleVO();
			BeanUtils.copyProperties(articleDTO, articleVO);
			articleVOs.add(articleVO);
		}
		return ResultUtil.sucess(articleVOs);
	}

}
