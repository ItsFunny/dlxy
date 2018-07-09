/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月5日 下午1:34:43
* 
*/
package com.dlxy.system.management.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.UserDTO;
import com.dlxy.system.management.utils.ManagementUtil;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月5日 下午1:34:43
*/
@Controller
public class TitleController
{
	@RequestMapping("/titles")
	public ModelAndView showTitles(@PathVariable(name="parentId",required=false)String parentIdStr,HttpServletRequest request,HttpServletResponse response)
	{
		Map<String, Object>params=new HashMap<>();
		UserDTO user=ManagementUtil.getLoginUser();
		
		params.put("user", user);
		
		ModelAndView modelAndView=new ModelAndView("01",params);
		return modelAndView;
	}
}
