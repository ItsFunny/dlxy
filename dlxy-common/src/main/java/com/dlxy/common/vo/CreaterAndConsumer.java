/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月27日 上午10:31:16
* 
*/
package com.dlxy.common.vo;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

/**
* 
* @When
* @Description
* @Detail
* @author joker 
* @date 创建时间：2018年7月27日 上午10:31:16
*/
public class CreaterAndConsumer
{
	private LinkedBlockingQueue<String>foods;
	
	public CreaterAndConsumer()
	{
		this.foods=new LinkedBlockingQueue<>();
	}
	public void test()
	{
		Producer[] producers=new Producer[5];
		Consumer[] consumers=new Consumer[10];
		for(int i=0;i<5;i++)
		{
			producers[i]=new Producer();
			new Thread(producers[i]).start();
		}
		for(int j=0;j<10;j++)
		{
			consumers[j]=new Consumer();
			new Thread(consumers[j]).start();
		}
	}
	public static void main(String[] args)
	{
		new CreaterAndConsumer().test();
	}
	class Producer implements Runnable
	{
		@Override
		public void run()
		{
			while(!Thread.currentThread().isInterrupted())
			{
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String food=UUID.randomUUID().toString();
				System.out.println("producer "+Thread.currentThread()+" create food "+food);
				try
				{
					foods.put(food);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	class Consumer implements Runnable
	{

		@Override
		public void run()
		{
			while(!Thread.currentThread().isInterrupted())
			{
				try
				{
					String food = foods.take();
					System.out.println("consumer:"+Thread.currentThread()+"get food:"+food);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		
	}
}
