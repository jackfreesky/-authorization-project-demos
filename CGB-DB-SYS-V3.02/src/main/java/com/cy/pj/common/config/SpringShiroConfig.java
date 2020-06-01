package com.cy.pj.common.config;
import java.util.LinkedHashMap;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cy.pj.sys.service.realm.ShiroUserRealm;
@Configuration
public class SpringShiroConfig {//applicationContext.xml
	
	@Bean
	public Realm realm() {
		return new ShiroUserRealm();
	}
	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition() {
	    DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
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
		filterChainDefinitionMap.put("/**", "user");
	    chainDefinition.addPathDefinitions(filterChainDefinitionMap);
	    return chainDefinition;
	}
	@Bean
	protected CacheManager shiroCacheManager() {
	    return new MemoryConstrainedCacheManager();
	}

}




