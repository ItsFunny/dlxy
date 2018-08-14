package com.dlxy.exception;

import com.dlxy.common.dto.SuspicionDTO;

/**
* 
* @When
* @Detail
* @author joker 
* @date 创建时间：2018年7月03日 下午4:47:37
*/
public class DlxySuspicionException extends RuntimeException
{

	private static final long serialVersionUID = 5170460825019109628L;
	private String msg;
	private SuspicionDTO suspicionDTO;
	public DlxySuspicionException()
	{
		super();
	}
	public DlxySuspicionException (String msg,SuspicionDTO suspicionDTO)
	{
		super(msg);
		this.msg=msg;
		this.suspicionDTO=suspicionDTO;
	}
	public DlxySuspicionException (SuspicionDTO suspicionDTO)
	{
		super();
		this.suspicionDTO=suspicionDTO;
	}
	public String getMsg()
	{
		return msg;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	public SuspicionDTO getSuspicionDTO()
	{
		return suspicionDTO;
	}
	public void setSuspicionDTO(SuspicionDTO suspicionDTO)
	{
		this.suspicionDTO = suspicionDTO;
	}
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
	
	
}
