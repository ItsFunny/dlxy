/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午8:47:32
* 
*/
package com.dlxy.system.management.controller;



import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.UserDTO;
import com.dlxy.server.user.service.IUserService;
import com.dlxy.system.management.config.shiro.DlxyShiroAuthToken;
import com.dlxy.system.management.constant.SessionConstant;
import com.dlxy.system.management.utils.ManagementUtil;
import com.google.code.kaptcha.Producer;
import com.joker.library.utils.KeyUtils;

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
	@Autowired
	Producer captchaProducer;
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/index")
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response)
	{
		UserDTO user = ManagementUtil.getLoginUser();
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("user",user);
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
		request.getSession(true).setAttribute(SessionConstant.LOGIN_KCODE, capText);
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
	public ModelAndView unauth(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView=new ModelAndView("unauth");
		modelAndView.addObject("user",ManagementUtil.getLoginUser());
		return modelAndView;
	}

	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response)
	{
//		UserDTO user = ManagementUtil.getLoginUser();
		ModelAndView modelAndView = new ModelAndView("login");
//		modelAndView.addObject("user",user);
		return modelAndView;
	}

	@RequestMapping("/public/doLogin")
	public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String Vcode=request.getParameter("vcode");
		Object o = request.getSession(true).getAttribute(SessionConstant.LOGIN_KCODE);
		if(null==o || StringUtils.isEmpty((String)o) ||! ((String)o).equals(Vcode))
		{
			modelAndView=new ModelAndView("login");
			modelAndView.addObject("error","验证码错误");
			return modelAndView;
		}
		UserDTO userDTO = userService.findByUsername(username);
		if(null==userDTO || !userDTO.getPassword().equals(KeyUtils.md5Encrypt(password)))
		{
			modelAndView=new ModelAndView("login");
			modelAndView.addObject("error","用户名不存在,或者密码错误,请重新输入");
		}else {
			SecurityUtils.getSubject().login(new DlxyShiroAuthToken(userDTO, password));
			modelAndView=new ModelAndView("redirect:/index.html");
		}
		return modelAndView;
	}
}
