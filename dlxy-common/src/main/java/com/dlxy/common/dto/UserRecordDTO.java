/**
*
* @Description
* @author joker 
* @date 创建时间：2018年6月29日 下午4:24:30
* 
*/
package com.dlxy.common.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年6月29日 下午4:24:30
 */
public class UserRecordDTO implements Serializable
{
	/**
	* 
	* @Description
	* @author joker 
	* @date 创建时间：2018年6月29日 下午4:25:35
	*/
	private static Pattern detailPattern=Pattern.compile("\\w+：\\w+：\\w+：");
	
//	public static void main(String[] args)
//	{
//		String string="a:2:3";
//		System.out.println(detailPattern.matcher(string).matches());
//	}
	
	private static final long serialVersionUID = 5911988410360555531L;
	private Long recordId;
	private Long userId;
	private String recordDetail;
	private Date createDate;
	//2018-07-03 13:59 add
	private String realname;
	
	//2018-07-24 13:54 add
//	private List<String>detail;
	private Map<String, Object>mapDetail;
	
	
	public UserRecordDTO()
	{
		this.createDate=new Date();
	}
	public static UserRecordDTO getUserRecordDTO(Long userId,String detail)
	{
		UserRecordDTO recordDTO=new UserRecordDTO();
		recordDTO.setUserId(userId);
		recordDTO.setRecordDetail(detail);
		recordDTO.setCreateDate(new Date());
		return recordDTO;
	}
	public String[] valid()
	{
		String[] strings = this.recordDetail.split(":");
		if(null==strings || strings.length<3)
		{
			throw new RuntimeException("操作成功,但是日志记录日志格式不正确,请联系管理员更改,稍后再试");
		}
		return strings;
//		try
//		{
//			Long.parseLong(strings[2]);
//		} catch (Exception e)
//		{
//			throw new RuntimeException("记录日志格式不正确,最后")
//		}
		
	}
	public Long getRecordId()
	{
		return recordId;
	}

	public void setRecordId(Long recordId)
	{
		this.recordId = recordId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getRecordDetail()
	{
		return recordDetail;
	}

	public void setRecordDetail(String recordDetail)
	{
		this.recordDetail = recordDetail;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}
	public String getRealname()
	{
		return realname;
	}
	public void setRealname(String realname)
	{
		this.realname = realname;
	}
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	public Map<String, Object> getMapDetail()
	{
		return mapDetail;
	}
	public void setMapDetail(Map<String, Object> mapDetail)
	{
		this.mapDetail = mapDetail;
	}
	
}
