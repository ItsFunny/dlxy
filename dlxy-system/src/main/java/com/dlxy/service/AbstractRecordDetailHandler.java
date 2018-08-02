/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月24日 上午10:34:10
* 
*/
package com.dlxy.service;

import java.util.Map;

import com.dlxy.common.dto.UserRecordDTO;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月24日 上午10:34:10
*/
public abstract class  AbstractRecordDetailHandler
{
	public static final String ARTICLE="article";
	
	public static final String TITLE="title";
	
	public static final String PICTURE="picture";
	
	public static final String USER="user";
	
	private final String level;
	
	
	protected AbstractRecordDetailHandler nextHandler;
	
	public AbstractRecordDetailHandler(String level)
	{
		this.level=level;
	}
	protected abstract Map<String, Object> parse(UserRecordDTO userRecordDTO);
	
	protected String getOthers(String[] strings)
	{
		String s="";
		for(int i=3;i<strings.length;i++)
		{
			s+=strings[i];
		}
		return s;
	}
	public void handler(UserRecordDTO userRecordDTO)
	{
		String[] strings = userRecordDTO.valid();
		String level=strings[1];
		if(level.equals(this.level))
		{
			userRecordDTO.setMapDetail(this.parse(userRecordDTO));
		}else if(null!=this.nextHandler){
			this.nextHandler.handler(userRecordDTO);
		}else {
			throw new RuntimeException("cant found  any matchable handlers");
		}
	}

	public String getLevel()
	{
		return level;
	}


	public AbstractRecordDetailHandler getNextHandler()
	{
		return nextHandler;
	}

	public void setNextHandler(AbstractRecordDetailHandler nextHandler)
	{
		this.nextHandler = nextHandler;
	}
	
}
