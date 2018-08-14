package com.dlxy.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.SessionListenerAdapter;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import com.dlxy.common.service.IdWorkerService;
import com.dlxy.common.service.IdWorkerServiceTwitter;
import com.dlxy.listener.ShiroSessionListener;
import com.dlxy.shiro.DlxyShiroRealm;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Configuration
@Order(1)
@EnableCaching
public class DlxySystemSpringConfiguration implements InitializingBean
{

	@Bean
	public DlxyBeanPostProcessor beanPostProcessor()
	{
		return new DlxyBeanPostProcessor();
	}
	@Bean
	public org.springframework.cache.CacheManager titlesCacheManager()
	{
		CompositeCacheManager compositeCacheManager=new CompositeCacheManager();
		List<org.springframework.cache.CacheManager>cacheManagers=new ArrayList<>();
		cacheManagers.add(new ConcurrentMapCacheManager("titles"));
		cacheManagers.add(new ConcurrentMapCacheManager("single_title"));
		cacheManagers.add(new ConcurrentMapCacheManager("links"));
		compositeCacheManager.setCacheManagers(cacheManagers);
		return compositeCacheManager;
	}
	@Bean
	public CacheManager cacheManager()
	{
		EhCacheManager ehCacheManager = new EhCacheManager();
		ehCacheManager.setCacheManagerConfigFile("classpath:shiro/cacheManager.xml");
		return ehCacheManager;
	}

	@Bean
	public org.apache.shiro.mgt.SecurityManager securityManager()
	{
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		defaultWebSecurityManager.setRealm(dlxyShiroRealm());
		defaultWebSecurityManager.setCacheManager(cacheManager());
		defaultWebSecurityManager.setSessionManager(sessionManager());
		return defaultWebSecurityManager;
	}

	@Bean
	public DlxyShiroRealm dlxyShiroRealm()
	{
		return new DlxyShiroRealm();
	}
	@Bean
	public ShiroSessionListener shiroSessionListener()
	{
		return new ShiroSessionListener();
	}
	@Bean
	public SessionManager sessionManager()
	{
		DefaultWebSessionManager defaultWebSessionManager=new DefaultWebSessionManager();
		defaultWebSessionManager.setGlobalSessionTimeout(1000*60*5);
		List<SessionListener>listeners=new ArrayList<>();
		listeners.add(shiroSessionListener());
		defaultWebSessionManager.setSessionListeners(listeners);
		return defaultWebSessionManager;
		
		
	}

	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean()
	{
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager());
		shiroFilterFactoryBean.setLoginUrl("/admin/login.html");
		shiroFilterFactoryBean.setSuccessUrl("/admin/index.html");
		shiroFilterFactoryBean.setUnauthorizedUrl("/public/unauth.html");
		// Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
		// filters.put("authc", new TFilter());
		Map<String, String> filterChainDefinitionMap = shiroFilterFactoryBean.getFilterChainDefinitionMap();
		filterChainDefinitionMap.put("/test/**", "anon");
		filterChainDefinitionMap.put("/admin/doLogin.html", "anon");
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
		filterChainDefinitionMap.put("/api/v1/admin/**", "authc");
		filterChainDefinitionMap.put("/admin/login.html", "authc");
		filterChainDefinitionMap.put("/admin/**", "authc");

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
	@Bean
	public MethodInvokingFactoryBean methodInvokingFactoryBean(SecurityManager securityManager)
	{
		MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
		factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
		factoryBean.setArguments(securityManager);
		return factoryBean;
	}

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

	@Override
	public void afterPropertiesSet() throws Exception
	{
		// ArticleVisitCountFactory.init(this.beanFactory.getBean(IRedisService.class),
		// this.beanFactory.getBean(IArticleService.class));
	}

	// @Bean
	// public MultipartResolver multipartResolver()
	// {
	// CommonsMultipartResolver commonsMultipartResolver = new
	// CommonsMultipartResolver();
	// commonsMultipartResolver.setMaxInMemorySize(1000000);
	// return commonsMultipartResolver;
	// }

}
