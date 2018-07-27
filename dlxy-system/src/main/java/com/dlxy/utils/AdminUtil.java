/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 上午9:02:15
* 
*/
package com.dlxy.utils;



import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.dlxy.common.dto.UserDTO;
import com.dlxy.enums.DlxytExceptionEnum;
import com.dlxy.exception.DlxySystemException;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月29日 上午9:02:15
*/
public class AdminUtil
{
	public static UserDTO getLoginUser()
	{
//		UserDTO userDTO=new UserDTO();
//		userDTO.setUserId(1L);
//		userDTO.setRealname("joker");
//		userDTO.setRoleId(0);
//		return userDTO;
		Subject subject = SecurityUtils.getSubject();
		UserDTO userDTO=(UserDTO)subject.getPrincipal();
		return userDTO;
	}
	public static void reloadUser(UserDTO userDTO)
	{
		Subject subject = SecurityUtils.getSubject();
		if(null!=subject && subject.isAuthenticated() && subject.getPrincipal() instanceof UserDTO)
		{
			 UserDTO prevUser=(UserDTO) subject.getPrincipal();
			 prevUser=userDTO;
		}
	}
}
