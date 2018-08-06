/**
*
* @author joker 
* @date 创建时间：2018年8月5日 上午8:04:27
* 
*/
package com.dlxy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author joker
 * @date 创建时间：2018年8月5日 上午8:04:27
 */
@Controller
@RequestMapping("/test")
public class TestController
{
	@RequiresRoles("admin")
	@RequestMapping("/t")
	@ResponseBody
	public String t()
	{
		return "oK";
	}
	
	
	@RequestMapping("/date")
	public ModelAndView testDate()
	{

		return new ModelAndView("date");
	}
	@RequestMapping("/doDate")
	public void testDate(HttpServletRequest request,HttpServletResponse response)
	{
		System.out.println(request.getParameter("startTime"));
	}

}
