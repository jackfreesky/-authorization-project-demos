package com.cy.pj.common.config;
import java.util.LinkedHashMap;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SpringShiroConfig {//applicationContext.xml
	/**
	 * 会话管理器对象配置
	 * @return
	 */
	@Bean
	public SessionManager sessionManager() {
		DefaultWebSessionManager sessionManager=new DefaultWebSessionManager();
		sessionManager.setGlobalSessionTimeout(60*60*1000);
		return sessionManager;
	}
	/**
	 * 记住我操作对象的配置
	 * @return
	 */
	@Bean
	public RememberMeManager rememberMeManager() {
		CookieRememberMeManager cManager=new CookieRememberMeManager();
		SimpleCookie simpleCookie=new SimpleCookie("rememberMe");
		simpleCookie.setMaxAge(7*24*60*60*1000);
		cManager.setCookie(simpleCookie);
		return cManager;
	}
	
	/**
	 * 配置shiro缓存管理器对象
	 */
	@Bean
	public CacheManager shiroCacheManager() {
		return new MemoryConstrainedCacheManager();
	}
	
	@Bean
	public SecurityManager securityManager(Realm realm,
			CacheManager cacheManager,
			RememberMeManager rememberMeManager,
			SessionManager sessionManager) {
		
		DefaultWebSecurityManager sManager=new DefaultWebSecurityManager();
		sManager.setRealm(realm);
		sManager.setCacheManager(cacheManager);
		sManager.setRememberMeManager(rememberMeManager);
		sManager.setSessionManager(sessionManager);
		return sManager;
	}
	/**
	 * 配置过滤器工厂Bean对象，此bean对象的作用就是要创建过滤器工厂，然后通过
	 * 过滤器工厂创建过滤器，通过过滤器对请求进行过滤。
	 * @return
	 */
	@Bean //<bean id="" class="">
	//@Autowired
	public ShiroFilterFactoryBean shiroFilterFactory(SecurityManager securityManager) {
		ShiroFilterFactoryBean sBean=new ShiroFilterFactoryBean();
		//设置安全管理器
		sBean.setSecurityManager(securityManager);
		//设置认证页面(登陆页面)
		sBean.setLoginUrl("/doLoginUI");
		//设置过滤规则
		LinkedHashMap<String,String> filterChainDefinitionMap=new LinkedHashMap<>();
		//设置匿名方法的资源
		filterChainDefinitionMap.put("/bower_components/**","anon");
		filterChainDefinitionMap.put("/build/**","anon");
		filterChainDefinitionMap.put("/dist/**","anon");
		filterChainDefinitionMap.put("/plugins/**","anon");
		filterChainDefinitionMap.put("/user/doLogin","anon");
		filterChainDefinitionMap.put("/doLogout","logout");//logout由官方定义，规则也是有官方说了算
		//设置需要认证访问的资源
		//filterChainDefinitionMap.put("/**", "authc");
		filterChainDefinitionMap.put("/**", "user");//改为用户端认证
		sBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return sBean;
	}
	/**
	 * Shiro框架在进行授权访问时是基于AOP思想进行实现的，在spring框架中原生AOP
	 * 的实现需要有一个对象Advisor对象。
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor  authorizationAttributeSourceAdvisor (
		    		    SecurityManager securityManager) {
	     AuthorizationAttributeSourceAdvisor advisor=
					new AuthorizationAttributeSourceAdvisor();
	     advisor.setSecurityManager(securityManager);
		 return advisor;
	}

}




