/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月29日 下午1:53:04
* 
*/
package com.dlxy.system.batch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.system.batch.service.IRedisService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月29日 下午1:53:04
*/
@RestController
@RequestMapping("/monitor")
public class MonitorController
{
	@Autowired
	private IRedisService redisService;
	
	@RequestMapping("/test")
	public String test()
	{
		boolean avaliable = redisService.isAvaliable();
		String json = redisService.get("ARTICLE_VISIT_COUNT:386208411710259200");
		if(avaliable)
		{
			return "ok";
		}else {
			return "fail";
		}
	}
}
