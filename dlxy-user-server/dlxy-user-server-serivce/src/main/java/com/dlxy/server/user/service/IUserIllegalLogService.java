/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 上午8:16:35
* 
*/
package com.dlxy.server.user.service;

import com.dlxy.server.user.model.DlxyUserIllegalLog;

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
	void addIllegalLog(DlxyUserIllegalLog illegalLog);
}
