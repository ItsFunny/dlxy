package com.dlxy.service;

import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public class UserRecordHandlerFactory
{
	private static AbstractRecordDetailHandler useRecordDetailHandler=new ArticleRecordDetailHandler(AbstractRecordDetailHandler.ARTICLE);
	static {
		TitleRecordDetailHandler titleRecordDetailHandler=new TitleRecordDetailHandler(AbstractRecordDetailHandler.TITLE);
		PictureRecordDetailHandler pictureRecordDetailHandler=new PictureRecordDetailHandler(AbstractRecordDetailHandler.PICTURE);
		UserRecordDetailHandler userRecordDetailHandler=new UserRecordDetailHandler(AbstractRecordDetailHandler.USER);
		useRecordDetailHandler.setNextHandler(titleRecordDetailHandler);
		titleRecordDetailHandler.setNextHandler(pictureRecordDetailHandler);
		pictureRecordDetailHandler.setNextHandler(userRecordDetailHandler);
	}
	
	public static AbstractRecordDetailHandler create()
	{
		return useRecordDetailHandler;
	}
}
