/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月14日 下午6:52:11
* 
*/
package com.dlxy.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.annotation.CheckIllegalFormat;
import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.SuspicionDTO;
import com.dlxy.common.enums.ArticleStatusEnum;
import com.dlxy.common.enums.ArticleTypeEnum;
import com.dlxy.common.enums.DlxyTitleEnum;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.common.event.AppEventPublisher;
import com.dlxy.common.vo.PageVO;
import com.dlxy.constant.TitleArticleConstant;
import com.dlxy.exception.DlxySuspicionException;
import com.dlxy.exception.DlxySystemIllegalException;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.service.IArticleWrappedService;
import com.dlxy.service.ITitleWrappedService;
import com.dlxy.vo.TitleDetailVO;
import com.joker.library.utils.CommonUtils;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月14日 下午6:52:11
*/
@Controller
public class PortalController
{
	private Logger logger=LoggerFactory.getLogger(PortalController.class);
	@Autowired
	private ITitleService titleService;
	@Autowired
	private ITitleWrappedService titleManagementWrappedService;
	@Autowired
	private IArticleWrappedService articleManagementWrappedService;
	@Autowired
	private IArticleService articleService;
	
	@Autowired
	private AppEventPublisher appeventPublisher;
	
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response)
	{
		/*
		 * 1.得到所有的类目
		 * 2.得到类目对应的文章
		 */
		Map<String, Object>params=new HashMap<String, Object>();
		ModelAndView modelAndView=null;
		DlxyTitleDTO dlxyTitleDTO=null;
		try
		{
			dlxyTitleDTO = titleManagementWrappedService.findDlxyDetailTitles(ITitleWrappedService.MAX_SHOW_ARTICLE_NUMBER);
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("[显示首页学院相关新闻]error:{}",e.getCause());
		}
		params.put("dlxyTitleDTO", dlxyTitleDTO);
		modelAndView=new ModelAndView("portal/index",params);
		return modelAndView;
	}
	
	@CheckIllegalFormat
	@RequestMapping("/title/detail/{titleId}")
	public ModelAndView showTitleDetail(@PathVariable("titleId")String titleIdStr,HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView=null;
		try
		{
			Integer titleId=Integer.parseInt(titleIdStr);
			int pageSize=Integer.parseInt(StringUtils.defaultString(request.getParameter("pageSize"), "10"));
			int pageNum=Integer.parseInt(StringUtils.defaultString(request.getParameter("pageNum"),"1"));
			//显示文章:
			TitleDetailVO titleDetailVO = articleManagementWrappedService.findTitleArticles(pageSize, pageNum, titleId);
			PageVO<Collection<ArticleDTO>>pageVO=new PageVO<Collection<ArticleDTO>>(titleDetailVO.getArticlePage().getData(), pageSize, pageNum, titleDetailVO.getArticlePage().getTotalCount());
			modelAndView=new ModelAndView("portal/title_detail");
			modelAndView.addObject("pageVO",pageVO);
			modelAndView.addObject("parent",titleDetailVO.getParentAndChilds());
			modelAndView.addObject("title",titleDetailVO.getTitleSelf());
			return modelAndView;
		} 
		catch (Exception e) {
			logger.error("[show title articles] error:{}",e.getMessage());
			
		}
		return modelAndView;
	}
	@CheckIllegalFormat
	@RequestMapping("/article/detail/{articleId}")
	public ModelAndView showArticleDetail(@PathVariable("articleId")String articleIdStr,HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView=null;
		Map<String, Object>params=new HashMap<>();
		try
		{
			Long articleId=Long.parseLong(articleIdStr);
			ArticleDTO articleDTO = articleManagementWrappedService.showArticleDetail(articleId);
			if(null==articleDTO || !articleDTO.getArticleStatus().equals(ArticleStatusEnum.UP.ordinal()))
			{
				params.put("error", "文章不存在或已下线");
				modelAndView=new ModelAndView("error",params);
				return modelAndView;
			}
			params.put("article", articleDTO);
//			Map<String, Object>p=new HashMap<>();
//			p.put("articleType", ArticleTypeEnum.INTRODUCE_ARTICLE.ordinal());
//			Collection<ArticleDTO> latest = articleService.findLatestArticleLimited(TitleArticleConstant.MAX_NUMBER_ARTICLES);
			
//			params.put("latestArticles", latest);
			modelAndView=new ModelAndView("portal/article_detail",params);
		} catch (Exception e)
		{
			params.put("error", e.getMessage());//记录消息
			logger.error("[show article detail] error:{}",e.getMessage());
			modelAndView=new ModelAndView("error",params);
		}
		return modelAndView;
	}
}

