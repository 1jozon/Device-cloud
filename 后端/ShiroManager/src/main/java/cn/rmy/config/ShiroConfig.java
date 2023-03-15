package cn.rmy.config;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Autowired RedisSessionDAO redisSessionDAO;
    //1、创建Realm对象，自定义
    @Bean(name="myRealm")
    public Realm myRealm(){
        return new MyRealm();
    }

    //2、DefaultWebSecurityManager
/*    @Bean
    public DefaultWebSecurityManager securityManager(Realm myRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(myRealm);
        return securityManager;
    }*/

    //3、ShiroFilterFactory
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();

        //设置安全管理器
        bean.setSecurityManager(securityManager);
        Map<String, Filter> filter = new LinkedHashMap<>();
        filter.put("authc", new ShiroFilter());
        //设置shiro内置过滤器，拦截
        Map<String,String> filterMap = new HashMap<>();
        filterMap.put("/cc/cc/getupdate","anon");
        //filterMap.put("/rmy/message/*","authc");
        //filterMap.put("/rmy/entry/register","authc");
        bean.setFilterChainDefinitionMap(filterMap);

        //如果无权限 跳转到登录页面
        bean.setLoginUrl("/login");
        //如过未授权，进行跳转到未授权页面
        //bean.setUnauthorizedUrl("noAuthc");
        bean.setFilters(filter);
        return bean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor attributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        //设置安全管理器
        attributeSourceAdvisor.setSecurityManager(securityManager);
        return attributeSourceAdvisor;
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);

        return defaultAdvisorAutoProxyCreator;
    }

    /*share - session*/

    @Bean
    public DefaultWebSessionManager defaultWebSessionManager(RedisSessionDAO redisSessionDAO){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager(){
            //重写touch（）方法，降低Session更新频率
            @Override
            public void touch(SessionKey key) throws InvalidSessionException {
                Session session = doGetSession(key);
                if(session != null){
                    long oldTime = session.getLastAccessTime().getTime();
                    session.touch();
                    long newTime = session.getLastAccessTime().getTime();
                    if(newTime - oldTime > 3000000){
                        onChange(session);
                    }
                }
            }
        };

        //绑定SessionDAO
        sessionManager.setSessionDAO(redisSessionDAO);

        SimpleCookie sessionIdCookie = new SimpleCookie("sessionId");
        sessionIdCookie.setPath("/");
        sessionIdCookie.setMaxAge(8 * 60 * 60); // 单位：秒数
        sessionManager.setSessionIdCookie(sessionIdCookie); // 绑定Cookie模版

        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setGlobalSessionTimeout(24 * 60 * 60 * 1000);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationInterval(2 * 60 * 60 * 1000);
        sessionManager.setDeleteInvalidSessions(true);

        return sessionManager;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(Realm myRealm, SessionManager sessionManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm);
        securityManager.setSessionManager(defaultWebSessionManager(redisSessionDAO));
        //securityManager.setSessionManager(sessionManager);
        return securityManager;
    }





}
