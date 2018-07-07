/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月8日 上午12:05:51
* 
*/
package com.dlxy.system.management.exception;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月8日 上午12:05:51
*/
public class ManagementIllegalException extends RuntimeException
{

	/**
	* 
	* @Description
	* @author joker 
	* @date 创建时间：2018年7月8日 上午12:08:42
	*/
	private static final long serialVersionUID = -758841428074965716L;
	
	private Long userId;
	
	private String detail;

}
