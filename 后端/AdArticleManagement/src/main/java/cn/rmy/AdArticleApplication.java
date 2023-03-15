package cn.rmy;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 广告条的应用程序
 *
 * @author chu
 * @date 2021/11/09
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableTransactionManagement(proxyTargetClass = true)
public class AdArticleApplication {
    public static void main(String[] args){
        SpringApplication.run(
                AdArticleApplication.class,args);
    }
}
