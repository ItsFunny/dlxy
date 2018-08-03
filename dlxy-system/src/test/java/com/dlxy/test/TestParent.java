/**
*
* @author joker 
* @date 创建时间：2018年8月3日 上午9:19:03
* 
*/
package com.dlxy.test;

import java.util.Observable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
* 
* @author joker 
* @date 创建时间：2018年8月3日 上午9:19:03
*/
@RunWith(SpringJUnit4ClassRunner.class)
public class TestParent
{
	@Test
	public void testParent()
	{
	
	}
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
		
		Observable bObservable = context.getBean(Observable.class);
		System.out.println(bObservable);
	}
}
