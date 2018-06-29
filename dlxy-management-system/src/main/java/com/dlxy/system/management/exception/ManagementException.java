/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午8:48:23
* 
*/
package com.dlxy.system.management.exception;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月29日 下午8:48:23
 */
public class ManagementException extends RuntimeException
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
	
	public ManagementException(ManagementExceptionEnum enum1)
	{
		super(enum1.getMsg());
		this.id=enum1.ordinal();
		this.msg=enum1.getMsg();
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

}
