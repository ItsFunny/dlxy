/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 上午9:02:15
* 
*/
package com.dlxy.utils;



import com.dlxy.common.dto.UserDTO;

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
		UserDTO userDTO=new UserDTO();
		userDTO.setUserId(1L);
		userDTO.setRealname("joker");
		userDTO.setRoleId(0);
		return userDTO;
//		UserDTO userDTO=(UserDTO) SecurityUtils.getSubject().getPrincipal();
//		if(null==userDTO)
//		{
//			throw new DlxySystemException(DlxytExceptionEnum.USER_NOT_LOGIN);
//		}
//		return userDTO;
	}
}
