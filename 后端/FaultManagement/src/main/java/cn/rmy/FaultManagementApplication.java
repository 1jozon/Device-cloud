package cn.rmy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableEurekaClient
//@EnableDiscoveryClient
//@EnableCircuitBreaker
public class FaultManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaultManagementApplication.class, args);
        System.out.println("FaultManagementApplication 启动");
    }

}
