/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月24日 下午3:53:28
* 
*/
package com.dlxy.service;

import org.apache.http.annotation.ThreadSafe;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月24日 下午3:53:28
*/
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
