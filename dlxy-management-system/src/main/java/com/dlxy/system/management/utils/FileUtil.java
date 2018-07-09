/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月9日 上午11:20:36
* 
*/
package com.dlxy.system.management.utils;

import java.io.File;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月9日 上午11:20:36
*/
public class FileUtil
{
	public static void delFileOrDir( File file)
	{
		boolean isDir=false;
		if(!file.exists())
		{
			return;
		}
		if(file.isDirectory())
		{
			isDir=true;
			File[] listFiles = file.listFiles();
			for (File file2 : listFiles)
			{
				if(file2.isDirectory())
				{
					delFileOrDir(file2);
				}else {
					file2.delete();
				}
			}
		}else {
			file.delete();
		}
		if(!isDir)
		{
			file.delete();
		}
		
	}
}
