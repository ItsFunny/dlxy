/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午7:24:15
* 
*/
package com.dlxy.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月28日 下午7:24:15
*/
public class AppEventLogPubliher implements AppEventPublisher
{
	private Logger logger=LoggerFactory.getLogger(AppEventLogPubliher.class);

	public void publish(AppEvent event)
	{
		logger.info("{}",event);
	}

}
