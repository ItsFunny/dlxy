/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 上午8:49:57
* 
*/
package com.dlxy.common.dto;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月29日 上午8:49:57
 */
public class ResultDTO<T>
{
	private Integer code;

	private String msg;

	private T data;

	public Integer getCode()
	{
		return code;
	}

	public void setCode(Integer code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public T getData()
	{
		return data;
	}

	public void setData(T data)
	{
		this.data = data;
	}

}
