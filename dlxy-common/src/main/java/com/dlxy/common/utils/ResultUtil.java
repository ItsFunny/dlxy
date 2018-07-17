/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 上午8:50:54
* 
*/
package com.dlxy.common.utils;

import com.dlxy.common.dto.ResultDTO;
import com.dlxy.common.enums.RestAPIStatus;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月29日 上午8:50:54
 */
public class ResultUtil
{
	public static <T> ResultDTO<T> needMoreOp(T data,String msg)
	{
		ResultDTO<T> resultDTO = new ResultDTO<T>();
		resultDTO.setData(data);
		resultDTO.setMsg(msg);
		resultDTO.setCode(RestAPIStatus.NEEDOP.ordinal());
		return resultDTO;
	}
	public static <T> ResultDTO<T> needMoreOp(String msg)
	{
		return needMoreOp(null, msg);
	}
	public static <T> ResultDTO<T> needMoreOp()
	{
		return needMoreOp("needMoreOperation");
	}
	public static <T> ResultDTO<T> sucess(T data, String msg)
	{
		ResultDTO<T> resultDTO = new ResultDTO<T>();
		resultDTO.setData(data);
		resultDTO.setMsg(msg);
		resultDTO.setCode(RestAPIStatus.SUCESS.ordinal());
		return resultDTO;
	}
	public static<T> ResultDTO<T>sucess(T data)
	{
		return sucess(data, "sucess");
	}
	public static <T> ResultDTO<T> sucess(String msg)
	{
		return sucess(null,msg);
	}
	public static <T> ResultDTO<T> sucess()
	{
		return sucess(null,"sucess");
	}

	public static <T> ResultDTO<T>fail()
	{
		return fail("fail");
	}
	public static <T> ResultDTO<T> fail(String msg)
	{
		ResultDTO<T> resultDTO = new ResultDTO<T>();
		resultDTO.setMsg(msg);
		resultDTO.setCode(RestAPIStatus.FAIL.ordinal());
		return resultDTO;
	}
}
