/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月7日 上午7:22:19
* 
*/
package com.dlxy.system.management.config.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRoleDTO;
import com.dlxy.server.user.service.IUserRoleService;
import com.dlxy.server.user.service.IUserService;
import com.joker.library.utils.CommonUtils;
import com.joker.library.utils.KeyUtils;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月7日 上午7:22:19
*/
public class DlxyShiroAuthRealm extends AuthorizingRealm
{
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IUserRoleService userRoleService;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0)
	{
		System.out.println("调用授权");
		UserDTO userDTO=(UserDTO) arg0.getPrimaryPrincipal();
		int roleId=userDTO.getRoleId();
		UserRoleDTO roleDTO = userRoleService.findByRoleId(roleId);
		SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
		if(null!=roleDTO)
		{
			authorizationInfo.addRole(roleDTO.getRoleName());
		}
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException
	{
		DlxyShiroAuthToken token=(DlxyShiroAuthToken) arg0;
//		String username=token.getUsername();
//		String password=token.getPassword();
//		UserDTO userDTO = userService.findByUsername(username);
//		if(!userDTO.getPassword().equals(KeyUtils.md5Encrypt(password)))
//		{
//			throw new UnknownAccountException("账户或者密码错误");
//		}
//		System.out.println("验证权限");
		AuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(),super.getName());
		return authenticationInfo;
	}
	
	
	
	
	

	@Override
	public boolean supports(AuthenticationToken token)
	{
		return token instanceof DlxyShiroAuthToken;
	}

}
