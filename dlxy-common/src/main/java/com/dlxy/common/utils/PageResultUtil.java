/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月30日 下午12:51:11
* 
*/
package com.dlxy.common.utils;

import java.util.Collections;

import com.dlxy.common.dto.PageDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年6月30日 下午12:51:11
*/
public class PageResultUtil
{
	public static <T> PageDTO<T> emptyPage()
	{
		PageDTO<T> pageDTO=new PageDTO<>();
		pageDTO.setTotalCount(0L);
		pageDTO.setData((T) Collections.emptyList());
		return pageDTO;
	}
}
