/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月24日 下午5:37:45
* 
*/
package com.dlxy.filter;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月24日 下午5:37:45
 */
public class Test
{
	public static void main(String[] args)
	{
		String string = "qwe-qqq";
		String string2 = string.replace("-", "%");
		String s = string2.replaceAll("%", ":");
		System.out.println(s);
		System.out.println(string2);
	}

}
