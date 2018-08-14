package com.dlxy.system.batch.service;

public interface IFileDeleteHandler
{
	Boolean delete(DeleteHandlerObject obj);

	String FTP_TYPE="1";
	String NORMAL_TYPE="2";
	
	
//	String FTP_IMG_STORE = "FTP_IMG_STORE";
//	String FTP_IMG_VISIT_PREFIX = "FTP_IMG_VISIT_PREFIX";
//	String LOCAL_IMG_STORE = "LOCAL_IMG_STORE";
//	String LOCAL_IMG_VISIT_PREFIX = "LOCAL_IMG_VISIT_PREFIX";
	
	static class DeleteHandlerObject
	{
		private String type;
		private String dbUrl;
		public String getType()
		{
			return type;
		}
		public void setType(String type)
		{
			this.type = type;
		}
		public String getDbUrl()
		{
			return dbUrl;
		}
		public void setDbUrl(String dbUrl)
		{
			this.dbUrl = dbUrl;
		}
	}
}
