package com.dlxy.controller;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.dlxy.common.dto.IllegalLogDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.enums.IllegalLevelEnum;
import com.dlxy.exception.DlxySystemIllegalException;
import com.dlxy.factory.WrappedServiceFactory;
import com.dlxy.service.ITitleWrappedService;
import com.dlxy.service.IUserWrappedService;
import com.dlxy.utils.AdminUtil;
import com.google.code.kaptcha.Producer;
import com.joker.library.utils.CommonUtils;

@Controller
public class PublicController
{
	@Autowired
	Producer captchaProducer;
	
	@Autowired
	private ITitleWrappedService titleWrappedService;
	@Autowired
	private IUserWrappedService userWrappedService;

	@RequestMapping("/public/test2")
	@ResponseBody
	public String test2()
	{
		return "ok";
	}
	
	@RequestMapping("/public/test")
	public ModelAndView test(HttpServletRequest request)
	{
		IllegalLogDTO illegalLogDTO = new IllegalLogDTO(CommonUtils.getIpAddr(request), 1L, "试图跨权访问所有文章",
				IllegalLevelEnum.Suspicious.ordinal());
		throw new DlxySystemIllegalException(illegalLogDTO);
	}

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

	@RequestMapping("/public/banned")
	public ModelAndView error(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException
	{
		ModelAndView modelAndView = new ModelAndView("error");
		String error = request.getParameter("error");
		error = StringUtils.isEmpty(error) ? "" : new String(error.getBytes("iso-8859-1"), "utf-8");
		modelAndView.addObject("error", error);
		return modelAndView;
	}

}
