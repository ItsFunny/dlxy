/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月14日 下午6:52:11
* 
*/
package com.dlxy.controller;

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

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.SuspicionDTO;
import com.dlxy.common.enums.ArticleTypeEnum;
import com.dlxy.common.enums.DlxyTitleEnum;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.common.vo.PageVO;
import com.dlxy.constant.TitleArticleConstant;
import com.dlxy.exception.DlxySuspicionException;
import com.dlxy.exception.DlxySystemIllegalException;
import com.dlxy.server.article.service.IArticleService;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.service.IArticleManagementWrappedService;
import com.dlxy.service.ITitleManagementWrappedService;
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
	private ITitleManagementWrappedService titleManagementWrappedService;
	@Autowired
	private IArticleManagementWrappedService articleManagementWrappedService;
	@Autowired
	private IArticleService articleService;
	
	
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response)
	{
		/*
		 * 1.得到所有的类目
		 * 2.得到类目对应的文章
		 */
		Map<String, Object>params=new HashMap<String, Object>();
		ModelAndView modelAndView=null;
		DlxyTitleDTO dlxyTitleDTO = titleManagementWrappedService.findDlxyDetailTitles();
		params.put("dlxyTitleDTO", dlxyTitleDTO);
		modelAndView=new ModelAndView("portal/index",params);
		return modelAndView;
	}
	
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
		} catch (NumberFormatException e)
		{
			//抛异常
//			throw new DlxySystemIllegalException(l)
			SuspicionDTO suspicionDTO=new SuspicionDTO(CommonUtils.getRemortIP(request), IllegalLevelEnum.Suspicious.ordinal(), request.getRequestURI());
			throw new DlxySuspicionException("", suspicionDTO);
		}catch (Exception e) {
			logger.error("[show title articles] error:{}",e.getMessage());
			
		}
		return modelAndView;
	}
	@RequestMapping("/article/detail/{articleId}")
	public ModelAndView showArticleDetail(@PathVariable("articleId")String articleIdStr,HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView=null;
		Map<String, Object>params=new HashMap<>();
		try
		{
			Long articleId=Long.parseLong(articleIdStr);
			ArticleDTO articleDTO = articleManagementWrappedService.showArticleDetail(articleId);
			params.put("article", articleDTO);
			//不知道为什么,前端ajax 获取到数据了,但是滚轮无法转动,所以这里也直接从这里取吧,可以解决,但不是我的范围了,交给前端的人解决
			Map<String, Object>p=new HashMap<>();
			p.put("articleType", ArticleTypeEnum.INTRODUCE_ARTICLE.ordinal());
			Collection<ArticleDTO> latest = articleService.findLatestArticleLimited(TitleArticleConstant.MAX_NUMBER_ARTICLES);
//			PageDTO<Collection<ArticleDTO>> dto = articleManagementWrappedService.findByParams(10, 1, p);
//			Collection<ArticleDTO> news = dto.getData();
			
			
			params.put("latestArticles", latest);
			modelAndView=new ModelAndView("portal/article_detail",params);
		} catch (Exception e)
		{
			params.put("error", "参数错误:");//记录消息
			logger.error("[show article detail] error:{}",e.getMessage());
			modelAndView=new ModelAndView("error",params);
		}
		return modelAndView;
	}
}

