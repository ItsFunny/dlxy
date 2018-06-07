/**
*
* @author joker 
* @date 创建时间：2018年6月7日 下午2:13:29
* 
*/
package com.dlxy.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
* 
* @author joker 
* @date 创建时间：2018年6月7日 下午2:13:29
*/
@Controller
public class TestController
{

	@RequestMapping("/test1")
	@ResponseBody
	public String test1()
	{
		return "a";
	}
	@RequestMapping("/test2")
	public ModelAndView test2()
	{
		return new ModelAndView("test");
	}
}
