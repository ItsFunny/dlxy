/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月9日 上午11:20:36
* 
*/
package com.dlxy.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

/**
 * if(file2.isDirectory()) // { // delFileOrDir(file2); // }else { //
 * file2.delete(); // }
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月9日 上午11:20:36
 */
public class FileUtil
{
	public static boolean delFileOrDir(File file)
	{
		if (!file.exists())
		{
			return true;
		}
		if (file.isDirectory())
		{
			File[] listFiles = file.listFiles();
			if(listFiles!=null)
			{
				for (File file2 : listFiles)
				{
					return delFileOrDir(file2);
				}
			}
			return file.delete();
		} else
		{
			return file.delete();
		}
	}
	public static String saveFile(MultipartFile file, Long articleId, HttpServletRequest request)
	{
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
		String realPath = request.getServletContext().getRealPath("");
		File dirFile = new File(realPath + File.separator + "imgs" + File.separator + articleId + File.separator);
		if (!dirFile.exists())
		{
			dirFile.mkdirs();
		}
		String fileName = UUID.randomUUID().toString();

		File newFiel = new File(dirFile.getAbsolutePath() + File.separator + fileName + suffix);
		try
		{
			file.transferTo(newFiel);
			StringBuffer reqUrl = request.getRequestURL();
			String requestURI = request.getRequestURI();
			String string = reqUrl.substring(0, reqUrl.indexOf(requestURI));
			// System.out.println(string+File.separator+"imgs"+File.separator+articleId+File.separator+fileName);
			String url = string + File.separator + "imgs" + File.separator + articleId + File.separator + fileName
					+ suffix;
			return url;
		} catch (IllegalStateException | IOException e)
		{
			e.printStackTrace();
			return null;
		}

	}
}
