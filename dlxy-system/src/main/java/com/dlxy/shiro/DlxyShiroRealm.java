/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月15日 下午4:14:25
* 
*/
package com.dlxy.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.common.dto.UserDTO;
import com.dlxy.common.dto.UserRoleDTO;
import com.dlxy.server.user.service.IUserRoleService;
import com.dlxy.server.user.service.IUserService;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月15日 下午4:14:25
*/
public class DlxyShiroRealm extends AuthorizingRealm
{

//	@Autowired
//	private IUserService userService;
	@Autowired
	private IUserRoleService userRoleService;
	
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
	{
		System.out.println("调用授权");
		UserDTO userDTO=(UserDTO) principals.getPrimaryPrincipal();
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
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
	{
		DlxyShiroAuthToken dlxyShiroAuthToken=(DlxyShiroAuthToken) token;
		SimpleAuthenticationInfo simpleAuthenticationInfo=new SimpleAuthenticationInfo(dlxyShiroAuthToken.getPrincipal(), dlxyShiroAuthToken.getCredentials(),getName());
		return simpleAuthenticationInfo;
	}

	@Override
	public boolean supports(AuthenticationToken token)
	{
		return token instanceof DlxyShiroAuthToken;
	}

}
