package cn.rmy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 统计应用程序
 *
 * @author chu
 * @date 2021/11/15
 */
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class CaliStatisticalApplication {
    public static void main(String[] args){
        SpringApplication.run(CaliStatisticalApplication.class,args);
    }
}
