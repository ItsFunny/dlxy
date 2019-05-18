/**
*
* @author joker 
* @date 创建时间：2018年8月5日 下午3:42:08
* 
*/
package com.dlxy.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.dlxy.server.article.service.IBeanSelefAware;

public class DlxyBeanPostProcessor implements BeanPostProcessor
{

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
	{
		
		
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
	{
		if(bean instanceof IBeanSelefAware)
		{
			IBeanSelefAware serviceImpl=(IBeanSelefAware) bean;
			serviceImpl.setSelf(serviceImpl);
			return serviceImpl;
		}
		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}
}
