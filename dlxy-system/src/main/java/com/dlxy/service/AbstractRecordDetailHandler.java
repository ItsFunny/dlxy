package com.dlxy.service;

import java.util.Map;

import com.dlxy.common.dto.UserRecordDTO;

public abstract class AbstractRecordDetailHandler
{
	public static final String ARTICLE = "article";

	public static final String TITLE = "title";

	public static final String PICTURE = "picture";

	public static final String USER = "user";
	
	public static final String LINK="link";

	private final String level;

	protected AbstractRecordDetailHandler nextHandler;

	public AbstractRecordDetailHandler(String level)
	{
		this.level = level;
	}

	protected abstract Map<String, Object> parse(UserRecordDTO userRecordDTO);

	protected String getOthers(String[] strings)
	{
		String s = "";
		for (int i = 3; i < strings.length; i++)
		{
			s += strings[i];
		}
		return s;
	}

	public void handler(UserRecordDTO userRecordDTO)
	{
		String[] strings = userRecordDTO.valid();
		String level = strings[1];
		if (level.equals(this.level))
		{
			userRecordDTO.setMapDetail(this.parse(userRecordDTO));
		} else if (null != this.nextHandler)
		{
			this.nextHandler.handler(userRecordDTO);
		} else
		{
			throw new RuntimeException("cant find  any matchable handlers");
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
