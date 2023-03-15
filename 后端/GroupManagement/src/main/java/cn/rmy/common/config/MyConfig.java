package cn.rmy.common.config;

import cn.rmy.common.redisUtils.EasySqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class MyConfig {

    //  乐观锁插件
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }


    //  分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }

//    @Bean
//    public ISqlInjector sqlInjector(){
//        return new LogicSqlInjector();
//    }

    @Bean
    public EasySqlInjector easySqlInjector(){
        return new EasySqlInjector();
    }


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


//    @Bean
//    @Profile({"dev","test"})// 设置 dev test 环境开启
//    public PerformanceInterceptor performanceInterceptor() {
//        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
//        performanceInterceptor.setMaxTime(100);// ms 设置sql执行的最大时间，如果超过就停止
//        performanceInterceptor.setFormat(true);
//        return new PerformanceInterceptor();
//    }

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(){
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        tomcatServletWebServerFactory.addConnectorCustomizers((Connector connector) -> {
            connector.setProperty("relaxedPathChars","\"[\\]^`{|}");
            connector.setProperty("relaxedQueryChars","\"[\\]^`{|}");
        });
        return tomcatServletWebServerFactory;
    }
}
