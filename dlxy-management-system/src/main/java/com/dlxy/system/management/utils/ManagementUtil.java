/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 上午9:02:15
* 
*/
package com.dlxy.system.management.utils;


import com.dlxy.common.dto.UserDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月29日 上午9:02:15
*/
public class ManagementUtil
{
	public static UserDTO getLoginUser()
	{
		UserDTO userDTO=new UserDTO();
		userDTO.setUserId(1L);
		userDTO.setUsername("joker");
		userDTO.setRoleId(0);
		return userDTO;
//		UserDTO userDTO=(UserDTO) SecurityUtils.getSubject().getPrincipal();
//		if(null==userDTO)
//		{
//			throw new ManagementException(ManagementExceptionEnum.USER_NOT_LOGIN);
//		}
//		return userDTO;
	}
}
