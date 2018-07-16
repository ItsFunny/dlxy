/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月15日 下午4:13:52
* 
*/
package com.dlxy.config;

import java.util.Map;
import java.util.Properties;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.dlxy.common.service.IdWorkerService;
import com.dlxy.common.service.IdWorkerServiceTwitter;
import com.dlxy.shiro.DlxyShiroRealm;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月15日 下午4:13:52
 */
@Configuration
public class DlxySystemShiroConfiguration
{

	@Bean
	public SecurityManager securityManager()
	{
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		defaultWebSecurityManager.setRealm(dlxyShiroRealm());
		return defaultWebSecurityManager;
	}

	@Bean
	public DlxyShiroRealm dlxyShiroRealm()
	{
		return new DlxyShiroRealm();
	}

	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean()
	{
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager());
		shiroFilterFactoryBean.setLoginUrl("/admin/login.html");
		shiroFilterFactoryBean.setSuccessUrl("/index.html");
		shiroFilterFactoryBean.setUnauthorizedUrl("/public/unauth.html");
		// Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
		// filters.put("authc", new TFilter());
		Map<String, String> filterChainDefinitionMap = shiroFilterFactoryBean.getFilterChainDefinitionMap();
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/common/**", "anon");
		filterChainDefinitionMap.put("/imgs/**", "anon");
		filterChainDefinitionMap.put("/images/**", "anon");
		filterChainDefinitionMap.put("/jsplug/**", "anon");
		filterChainDefinitionMap.put("/lang/**", "anon");
		filterChainDefinitionMap.put("/public/**", "anon");
		filterChainDefinitionMap.put("/plugins/**", "anon");
		filterChainDefinitionMap.put("/portal/**", "anon");
		filterChainDefinitionMap.put("/themes", "anon");
		filterChainDefinitionMap.put("/kindeditor-all.js", "anon");
		filterChainDefinitionMap.put("/kindeditor-all-min.js", "anon");
		// filterChainDefinitionMap.put("/api/**", "authc");
		// filterChainDefinitionMap.put("/user/**","authc");
		filterChainDefinitionMap.put("/**", "authc");
		filterChainDefinitionMap.put("/admin/doLogin.html", "authc");
		return shiroFilterFactoryBean;
	}

	@Bean(name = "lifeCycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor()
	{
		return new LifecycleBeanPostProcessor();
	}

	// /*
	// * 将securityManger绑定
	// */
	// @Bean
	// public MethodInvokingFactoryBean methodInvokingFactoryBean()
	// {
	// MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
	// factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
	// factoryBean.setArguments(securityManager());
	// return factoryBean;
	// }
	// /*
	// * 开启注解支持
	// */
	@DependsOn(value =
	{ "lifeCycleBeanPostProcessor" })
	@Bean(name = "defaultAdvisorAutoProxyCreator")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator()
	{
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	//
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager)
	{
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	@Bean
	public Producer kaptcha()
	{
		Properties p = new Properties();
		p.setProperty(Constants.KAPTCHA_BORDER, "no");
		p.setProperty(Constants.KAPTCHA_BORDER_COLOR, "105,179,90");
		p.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "14,78,149");
		p.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "487");
		p.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "68");
		p.setProperty(Constants.KAPTCHA_SESSION_KEY, "kcode");
		p.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
		p.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "宋体,楷体,微软雅黑");

		DefaultKaptcha kaptcha = new DefaultKaptcha();
		kaptcha.setConfig(new Config(p));
		return kaptcha;
	}

	@Bean
	public IdWorkerService IdWorkerService()
	{
		return new IdWorkerServiceTwitter(0, 1);
	}

	@Bean
	public MultipartResolver multipartResolver()
	{
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setMaxInMemorySize(1000000);
		return commonsMultipartResolver;
	}

	@Bean
	public JedisPool jedisPool()
	{
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(5);
		jedisPoolConfig.setMaxTotal(1024);
		jedisPoolConfig.setMaxWaitMillis(10000);
		jedisPoolConfig.setTestOnBorrow(true);
		// JedisPool jedisPool=new
		// JedisPool(jedisPoolConfig,dlxyProperty.getRedisHost(),dlxyProperty.getRedisPort(),10000,dlxyProperty.getRedisPassword());
		JedisPool jedisPool = new JedisPool("localhost", 6379);
		return jedisPool;
	}

}
