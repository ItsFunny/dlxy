/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 上午8:16:35
* 
*/
package com.dlxy.server.user.service;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月1日 上午8:16:35
*/
public interface IUserIllegalLogService
{
	void addIllegalLog(Long userId,String detail,int level);
}
