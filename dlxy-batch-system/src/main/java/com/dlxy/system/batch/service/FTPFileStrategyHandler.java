package com.dlxy.system.batch.service;


import java.io.File;

import com.joker.library.file.IFileStrategy;

public class FTPFileStrategyHandler extends AbstractDeleteFileHandler
{

	public FTPFileStrategyHandler(String type)
	{
		super(type);
	}

	@Override
	protected boolean handler(DeleteHandlerObject obj)
	{
		String baseStoreUrl=super.context.getStoreBasePath(IFileStrategy.IMG_TYPE);
		String visitPrefix=super.context.getVisitPrefix(IFileStrategy.IMG_TYPE);
		String filePath=baseStoreUrl+File.separator+obj.getDbUrl().substring(obj.getDbUrl().indexOf(visitPrefix));
		return super.context.delete(filePath);
	}
	

}
