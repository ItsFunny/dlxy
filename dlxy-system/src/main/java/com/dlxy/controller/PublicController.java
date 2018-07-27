/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月24日 下午7:06:29
* 
*/
package com.dlxy.controller;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.UserDTO;
import com.dlxy.utils.AdminUtil;
import com.google.code.kaptcha.Producer;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月24日 下午7:06:29
 */
@Controller
public class PublicController
{
	@Autowired
	Producer captchaProducer;

	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		UserDTO user = AdminUtil.getLoginUser();
		if (null == user)
		{
			modelAndView = new ModelAndView("login");
		} else
		{
			modelAndView = new ModelAndView("admin/index");
		}
		return modelAndView;
	}

	@RequestMapping("/public/imgcode")
	public ModelAndView generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		response.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		// return a jpeg
		response.setContentType("image/jpeg");
		// create the text for the image
		String capText = captchaProducer.createText();
		// store the text in the session
		request.getSession(true).setAttribute("kcode", capText);
		// create the image with the text
		BufferedImage bi = captchaProducer.createImage(capText);
		ServletOutputStream out = response.getOutputStream();
		// write the data out
		ImageIO.write(bi, "JPEG", out);
		try
		{
			out.flush();
		} finally
		{
			out.close();
		}
		return null;
	}

	@RequestMapping("/public/unauth")
	public ModelAndView unauth(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("no_permission");
		modelAndView.addObject("user", AdminUtil.getLoginUser());
		return modelAndView;
	}

}
