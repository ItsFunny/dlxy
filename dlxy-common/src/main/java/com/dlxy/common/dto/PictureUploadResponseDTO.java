/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月1日 下午6:24:59
* 
*/
package com.dlxy.common.dto;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月1日 下午6:24:59
 */
public class PictureUploadResponseDTO
{
	private Integer error;
	private String url;
	private String message;

	public Integer getError()
	{
		return error;
	}

	public void setError(Integer error)
	{
		this.error = error;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

}
