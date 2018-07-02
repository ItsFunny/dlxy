/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月2日 下午3:24:18
* 
*/
package com.dlxy.system.management.controller;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.common.dto.ArticleDTO;
import com.dlxy.common.dto.PageDTO;
import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.vo.PageVO;
import com.dlxy.server.user.service.IUserRecordService;
import com.dlxy.system.management.service.IUserMangementWrappedService;
import com.dlxy.system.management.utils.ManagementUtil;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月2日 下午3:24:18
 */
@Controller
@RequestMapping("/user")
public class UserController
{
	@Autowired
	private IUserMangementWrappedService userManagementWrappedService;

	@RequestMapping("/my/releases")
	public ModelAndView showMyRelases(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize,
			@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum)
	{
		UserDTO user = ManagementUtil.getLoginUser();
		ModelAndView modelAndView = null;
		Map<String, Object> params = new HashMap<>();
		params.put("userId", user.getUserId());
		String articleStatusStr = request.getParameter("articleStatus");
		if (!StringUtils.isEmpty(articleStatusStr))
		{
			try
			{
				params.put("articleStatus", Integer.parseInt(articleStatusStr));
			} catch (NumberFormatException e)
			{
				e.printStackTrace();
				params.put("error", "illegal argument :articleStatus");
			}
		}
		PageDTO<Collection<Map<String, Object>>> pageDTO=null;
		try
		{
			pageDTO = userManagementWrappedService.findByPage(pageSize, pageNum, params);
		} catch (SQLException e)
		{
			e.printStackTrace();
			params.put("error", e.getMessage());
		}
		if (params.containsKey("error"))
		{
			modelAndView = new ModelAndView("error", params);
		}else {
			PageVO<Collection<Map<String, Object>>> pageVO = new PageVO<>(pageDTO.getData(),
					pageSize, pageNum, pageDTO.getTotalCount());
			params.put("pageVO", pageVO);
			modelAndView=new ModelAndView("my_releases",params);
		}
		return modelAndView;
	}

}
