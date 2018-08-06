/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月9日 上午10:45:01
* 
*/
package com.dlxy.system.batch.constants;


import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月9日 上午10:45:01
*/
public interface PictureConstant
{
	String PICTURE_SAVE_LOCATION="";
	public static void main(String[] args)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -7);
		System.out.println(sdf.format(c.getTime()));
	}
}
