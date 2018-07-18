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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.DlxyTitleDTO;
import com.dlxy.common.enums.DlxyTitleEnum;
import com.dlxy.server.article.service.ITitleService;
import com.dlxy.service.ITitleManagementWrappedService;

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
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response)
	{
		Map<String, Object>params=new HashMap<String, Object>();
		ModelAndView modelAndView=null;
		DlxyTitleDTO dlxyTitleDTO = titleManagementWrappedService.findDlxyDetailTitles();
		params.put("dlxyTitleDTO", dlxyTitleDTO);
		modelAndView=new ModelAndView("portal/index",params);
		return modelAndView;
	}
	
	@RequestMapping("/title/detail/{titleId}")
	public ModelAndView showTitleDetail(@PathVariable("titleId")Integer titleId,HttpServletRequest requestq,HttpServletResponse response)
	{
		ModelAndView modelAndView=null;
		
		return modelAndView;
	}
}

