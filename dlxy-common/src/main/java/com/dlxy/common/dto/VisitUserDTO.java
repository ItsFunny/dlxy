/**
*
* @author joker 
* @date 创建时间：2018年6月27日 上午9:30:01
* 
*/
package com.dlxy.common.dto;

import java.util.List;

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
	private List<Long> articleIdList;

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

	public List<Long> getArticleIdList()
	{
		return articleIdList;
	}

	public void setArticleIdList(List<Long> articleIdList)
	{
		this.articleIdList = articleIdList;
	}
	

}
