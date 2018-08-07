/**
*
* @author joker 
* @date 创建时间：2018年8月7日 下午7:50:49
* 
*/
package com.dlxy.interceptor;

/**
* 
* @author joker 
* @date 创建时间：2018年8月7日 下午7:50:49
*/
public class Test
{
	private class Inner 
	{
		
	}
	public void test()
	{
		Inner nInner=this.new Inner();
		System.out.println("1");
	}
	public static void main(String[] args)
	{
		Test test=new Test();
		test.test();
	}
}
