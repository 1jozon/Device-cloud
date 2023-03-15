package cn.rmy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 质控分析
 *
 * @author chu
 * @date 2021/12/21
 */
@SpringBootApplication
@EnableAsync
public class QualityAnalysis {
    public static void main(String[] args){
        SpringApplication.run(QualityAnalysis.class);
    }
}
