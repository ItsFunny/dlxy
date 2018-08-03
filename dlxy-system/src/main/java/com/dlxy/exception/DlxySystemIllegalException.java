/**
*
* @author joker 
* @date 创建时间：2018年7月8日 上午12:05:51
* 
*/
package com.dlxy.exception;

import com.dlxy.common.dto.IllegalLogDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月8日 上午12:05:51
*/
public class DlxySystemIllegalException extends RuntimeException
{

	/**
	* 
	* @Description
	* @author joker 
	* @date 创建时间：2018年7月8日 上午12:08:42
	*/
	private static final long serialVersionUID = -758841428074965716L;
	
	private IllegalLogDTO illegalLogDTO;
	
	public DlxySystemIllegalException(IllegalLogDTO l)
	{
		super();
		this.illegalLogDTO=l;
	}
	public DlxySystemIllegalException(String msg,IllegalLogDTO l)
	{
		super(msg);
		this.illegalLogDTO=l;
	}
	
	public DlxySystemIllegalException()
	{
		super();
	}




	public IllegalLogDTO getIllegalLogDTO()
	{
		return illegalLogDTO;
	}

	public void setIllegalLogDTO(IllegalLogDTO illegalLogDTO)
	{
		this.illegalLogDTO = illegalLogDTO;
	}

	

}
