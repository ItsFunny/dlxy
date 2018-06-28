/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午8:47:32
* 
*/
package com.dlxy.system.management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月28日 下午8:47:32
 */
@Controller
public class IndexController
{
	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView=new ModelAndView("index");
		return modelAndView;
	}
}
