/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月28日 下午11:02:56
* 
*/
package com.dlxy.common.dto;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月28日 下午11:02:56
 */
public class PageDTO<T>
{
	private Long totalCount;
	private T data;
		
	
	public PageDTO(Long totalCount, T data)
	{
		super();
		this.totalCount = totalCount;
		this.data = data;
	}

	public Long getTotalCount()
	{
		return totalCount;
	}

	public void setTotalCount(Long totalCount)
	{
		this.totalCount = totalCount;
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
