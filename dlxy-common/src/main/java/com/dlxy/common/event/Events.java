/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午4:05:53
* 
*/
package com.dlxy.common.event;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月29日 下午4:05:53
*/
public enum Events
{
	UserRecordLog,
	
	UserIllegalLog,
	
	//用于白名单,黑名单的设置
	IpCheck,
	PicDelete,
	
	ArticleVisitCount,//用于更新article中的访问人数记录
}
