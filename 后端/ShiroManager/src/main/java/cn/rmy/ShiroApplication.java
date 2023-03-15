package cn.rmy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * shiro的应用程序
 *
 * @author chu
 * @date 2021/11/15
 */
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class ShiroApplication {
    public static void main(String[] args) {

        SpringApplication.run(ShiroApplication.class,args);
    }
}
