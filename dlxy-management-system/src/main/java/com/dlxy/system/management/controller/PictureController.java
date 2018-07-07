/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月7日 下午12:56:26
* 
*/
package com.dlxy.system.management.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.PictureDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.enums.PictureStatusEnum;
import com.dlxy.common.enums.PictureTypeEnum;
import com.dlxy.server.picture.service.IPictureService;
import com.dlxy.system.management.service.IPictureManagementWrappedService;
import com.dlxy.system.management.utils.ManagementUtil;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月7日 下午12:56:26
 */
@Controller
@RequestMapping("/picture")
public class PictureController
{
	@Autowired
	private IPictureService pictureService;
	
	@Autowired
	private IPictureManagementWrappedService pictureManagementWrappedService;
	
	private Logger logger=LoggerFactory.getLogger(PictureController.class);

	@RequestMapping("/edit")
	public ModelAndView editPortalPicture(HttpServletRequest request, HttpServletResponse response)
	{
		UserDTO userDTO = ManagementUtil.getLoginUser();
		ModelAndView modelAndView = null;
		try
		{
			Collection<PictureDTO> pictures = pictureService.findByStatus(PictureTypeEnum.Portal_Carousel.ordinal());
			modelAndView = new ModelAndView("portal_pictures_edit");
			modelAndView.addObject("user", userDTO);
			modelAndView.addObject("pictures", pictures);
		} catch (Exception e)
		{
			modelAndView = new ModelAndView("error");
			modelAndView.addObject("error", e.getMessage());
		}
		return modelAndView;
	}

	@RequestMapping("/upload/portalPics")
	public ModelAndView uploadPortalImgs(@RequestParam("imgFile") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response)
	{
		ModelAndView modelAndView = null;
		Map<String, Object>params=new HashMap<String, Object>();
		String pictureDisplaySeq=request.getParameter("pictureDisplaySeq");
		if(StringUtils.isEmpty(pictureDisplaySeq))
		{
			params.put("error", "missing argument:pictureDisplaySeq");
			modelAndView=new ModelAndView("error",params);
			return modelAndView;
		}
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		System.out.println(suffix);
		String t="portal"+File.separator+"imgs";
		String realPath = request.getServletContext().getRealPath(t);
		String id=UUID.randomUUID().toString();
		String name=id+suffix;
		File dirFile=new File(realPath);
		if(!dirFile.exists())
		{
			dirFile.mkdirs();
		}
		File newFile=new File(dirFile.getAbsolutePath()+File.separator+name);
		try
		{
			file.transferTo(newFile);
			String url = request.getRequestURL().substring(0,request.getRequestURL().indexOf(request.getRequestURI()));
			url+=File.separator+t+File.separator+name;
			PictureDTO pictureDTO=new PictureDTO();
			pictureDTO.setPictureType(PictureTypeEnum.Portal_Carousel.ordinal());
			pictureDTO.setPictureStatus(PictureStatusEnum.Effective.ordinal());
			pictureDTO.setPictureDisplaySeq(Integer.parseInt(pictureDisplaySeq));
//			pictureDTO.setPictureId(id);
			pictureDTO.setPictureUrl(url);
			pictureManagementWrappedService.addPortalPicture(ManagementUtil.getLoginUser().getUserId(), pictureDTO);
		} catch (IllegalStateException | IOException e)
		{
			e.printStackTrace();
			logger.error("[add portal-picture] error :{}",e.getMessage());
			params.put("error", "未知异常:"+e.getMessage());
		
		} catch (SQLException e)
		{
			e.printStackTrace();
			logger.error("[add portal-picture] occur SQLException {}",e.getMessage());
			params.put("error", "未知异常:"+e.getMessage());
		}
		if(params.containsKey("error"))
		{
			modelAndView=new ModelAndView("error",params);
			newFile.deleteOnExit();
		}else {
			modelAndView=new ModelAndView("redirect:/picture/edit.html",params);
		}
		
		return modelAndView;
	}

}
