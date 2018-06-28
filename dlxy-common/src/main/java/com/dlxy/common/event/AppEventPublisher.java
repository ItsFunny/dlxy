/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午7:23:39
* 
*/
package com.dlxy.common.event;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 下午7:23:39
*/
public interface AppEventPublisher
{
	void publish(AppEvent event);
}
