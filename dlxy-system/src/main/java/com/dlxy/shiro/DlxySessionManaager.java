/**
*
* @author joker 
* @date 创建时间：2018年8月15日 下午1:02:51
* 
*/
package com.dlxy.shiro;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

/**
* 
* @author joker 
* @date 创建时间：2018年8月15日 下午1:02:51
*/
public class DlxySessionManaager extends MemorySessionDAO
{

	@Override
	protected Session doReadSession(Serializable sessionId)
	{
		return super.doReadSession(sessionId);
	}

}
