/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午8:48:23
* 
*/
package com.dlxy.exception;

import com.dlxy.enums.DlxytExceptionEnum;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月29日 下午8:48:23
 */
public class DlxySystemException extends RuntimeException
{

	/**
	 * 
	 * @Description
	 * @author joker
	 * @date 创建时间：2018年6月29日 下午8:48:33
	 */
	private static final long serialVersionUID = 9067397916446094164L;
	private String msg;
	private Integer id;
	
	
	
	public DlxySystemException(DlxytExceptionEnum enum1)
	{
		super(enum1.getMsg());
		this.id=enum1.ordinal();
		this.msg=enum1.getMsg();
	}
	

}
