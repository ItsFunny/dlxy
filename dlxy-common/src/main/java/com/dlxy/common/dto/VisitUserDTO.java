/**
*
* @author joker 
* @date 创建时间：2018年6月27日 上午9:30:01
* 
*/
package com.dlxy.common.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
* 
* @When 用户第一次访问的时候
* @Description	用于文章访问人数的统计
* @Detail 通过文章id与articleIdList进行匹配
* @author joker 
* @date 创建时间：2018年6月27日 上午9:39:01
*/
public class VisitUserDTO
{
	private Long visitUserId;
	private String visitUserIp;
	private Map<Long, Long> record;

	public Long getLastVisitDate(Long articleId)
	{
		Long long1 = record.get(articleId);
		if(null==long1)
		{
			record.put(articleId, System.currentTimeMillis());
			return null;
		}else {
			return long1;
		}
	}
	public VisitUserDTO()
	{
		this.record=new ConcurrentHashMap<>();
	}
	public Long getVisitUserId()
	{
		return visitUserId;
	}
	

	public void setVisitUserId(Long visitUserId)
	{
		this.visitUserId = visitUserId;
	}

	public String getVisitUserIp()
	{
		return visitUserIp;
	}

	public void setVisitUserIp(String visitUserIp)
	{
		this.visitUserIp = visitUserIp;
	}

	public Map<Long, Long> getRecord()
	{
		return record;
	}

	public void setRecord(Map<Long, Long> record)
	{
		this.record = record;
	}

	

}
