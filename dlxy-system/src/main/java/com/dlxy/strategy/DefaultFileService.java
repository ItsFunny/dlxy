/**
*
* @author joker 
* @date 创建时间：2018年8月8日 下午4:47:02
* 
*/
package com.dlxy.strategy;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * 默认的文件上传,上传于本地
 * 
 * @author joker
 * @date 创建时间：2018年8月8日 下午4:47:02
 */
public class DefaultFileService extends AbstractFIleStrategy
{
	
	@Override
	public String upload(MultipartFile file, String storePath ,String newFileName,String key)
	{
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
		File dirFile = new File(storePath);
		if (!dirFile.exists())
		{
			dirFile.mkdirs();
		}
		File newFiel = new File(dirFile.getAbsolutePath() + File.separator + newFileName + suffix);
		try
		{
			file.transferTo(newFiel);
			return 	storePath.substring(storePath.indexOf(getVisitPrefix(key)))+File.separator+newFileName+suffix;
		} catch (IllegalStateException | IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
