/**
*
* @author joker 
* @date 创建时间：2018年8月1日 下午1:52:22
* 
*/
package com.dlxy.common.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author joker
 * @date 创建时间：2018年8月1日 下午1:52:22
 */
public class VisitUserHistoryDTO
{
	private String ip;
	
	

	private Map<String, HistroyDetail> details;
	
	public Integer getRefreshCounts(String uri)
	{
		HistroyDetail detail = this.details.get(uri);
		return detail.getRefreshCounts();
	}
	public Long getLastVisitTimes(String uri)
	{
		HistroyDetail detail=this.details.get(uri);
		return detail.getLastVisitTime();
	}
	
	public VisitUserHistoryDTO()
	{
		this.details=new HashMap<String, VisitUserHistoryDTO.HistroyDetail>();
	}
	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public Map<String, HistroyDetail> getDetails()
	{
		return details;
	}

	public void setDetails(Map<String, HistroyDetail> details)
	{
		this.details = details;
	}
	public static class HistroyDetail
	{
		private Integer refreshCounts=0;
		private Long lastVisitTime;
		//初始为5次
		private Integer maxLimitedCounts=5;
		
		public void incr()
		{
			this.refreshCounts++;
		}
		public Integer getRefreshCounts()
		{
			return refreshCounts;
		}
		public void setRefreshCounts(Integer refreshCounts)
		{
			this.refreshCounts = refreshCounts;
		}
		public Long getLastVisitTime()
		{
			return lastVisitTime;
		}
		public void setLastVisitTime(Long lastVisitTime)
		{
			this.lastVisitTime = lastVisitTime;
		}
		public Integer getMaxLimitedCounts()
		{
			return maxLimitedCounts;
		}
		public void setMaxLimitedCounts(Integer maxLimitedCounts)
		{
			this.maxLimitedCounts = maxLimitedCounts;
		}
	}

}
