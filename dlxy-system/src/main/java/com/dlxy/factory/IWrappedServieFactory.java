/**
*
* @author joker 
* @date 创建时间：2018年8月2日 下午3:31:46
* 
*/
package com.dlxy.factory;

import com.dlxy.defaultp.IWrappedService;

/**
* 
* @author joker 
* @date 创建时间：2018年8月2日 下午3:31:46
*/
public interface IWrappedServieFactory
{
	IWrappedService create(String name);
}
