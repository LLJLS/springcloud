package com.kevin.shiro.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.config.ShiroAnnotationProcessorConfiguration;
import org.apache.shiro.spring.config.ShiroBeanConfiguration;
import org.apache.shiro.spring.web.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

@Configuration
@Import({ShiroBeanConfiguration.class,
        ShiroAnnotationProcessorConfiguration.class,
        ShiroWebConfiguration.class,
        ShiroWebFilterConfiguration.class,
        ShiroRequestMappingConfig.class})
public class ShiroConfig {

    @Autowired
    private SecurityManager securityManager;

    @Bean
    public Realm realm() {
        return null;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        // logged in users with the 'admin' role
        chainDefinition.addPathDefinition("/data/list", "authc, roles[admin]");
        chainDefinition.addPathDefinition("/data/add", "roles[admin]");
        chainDefinition.addPathDefinition("/data/update", "roles[admin]");
        chainDefinition.addPathDefinition("/data/del", "roles[admin]");

        // logged in users with the 'document:read' permission
        chainDefinition.addPathDefinition("/docs/**", "authc, perms[document:read]");

        // all other paths require a logged in user
        chainDefinition.addPathDefinition("/**", "authc");
        return chainDefinition;
    }

    @PostConstruct
    private void initStaticSecurityManager() {
        SecurityUtils.setSecurityManager(securityManager);
    }
}
