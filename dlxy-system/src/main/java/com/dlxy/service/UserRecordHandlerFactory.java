package com.dlxy.service;

import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public class UserRecordHandlerFactory
{
	private static AbstractRecordDetailHandler articleHandler=new ArticleRecordDetailHandler(AbstractRecordDetailHandler.ARTICLE);
	static {
		TitleRecordDetailHandler titleRecordDetailHandler=new TitleRecordDetailHandler(AbstractRecordDetailHandler.TITLE);
		PictureRecordDetailHandler pictureRecordDetailHandler=new PictureRecordDetailHandler(AbstractRecordDetailHandler.PICTURE);
		UserRecordDetailHandler userRecordDetailHandler=new UserRecordDetailHandler(AbstractRecordDetailHandler.USER);
		LinkRecordDetailHandler linkRecordDetailHandler=new LinkRecordDetailHandler(AbstractRecordDetailHandler.LINK);
		articleHandler.setNextHandler(titleRecordDetailHandler);
		titleRecordDetailHandler.setNextHandler(pictureRecordDetailHandler);
//		pictureRecordDetailHandler.setNextHandler(userRecordDetailHandler);
//		useRecordDetailHandler.setNextHandler(linkRecordDetailHandler);
		pictureRecordDetailHandler.setNextHandler(userRecordDetailHandler);
		userRecordDetailHandler.setNextHandler(linkRecordDetailHandler);
	}
	
	public static AbstractRecordDetailHandler create()
	{
		return articleHandler;
	}
}
