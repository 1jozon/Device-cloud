package cn.rmy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableEurekaClient
//@EnableDiscoveryClient
//@EnableCircuitBreaker
public class ReagentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReagentServiceApplication.class, args);
        System.out.println("ReagentServiceApplication 启动");
    }

}
