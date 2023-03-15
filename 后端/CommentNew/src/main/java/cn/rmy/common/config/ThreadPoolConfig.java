package cn.rmy.common.config;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author chu
 * @date 2022/05/25
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolConfig.class);

    /**
     * 核心池大小
     */
    private  static final int CORE_POOL_SIZE = 50;

    /**
     * 最大池大小
     */
    private  static final int MAX_POOL_SIZE = 50;

    /**
     * 队列容量
     */
    private  static final int QUEUE_CAPACITY = 200;

    /**
     * 维持几秒钟
     */
    private  static final int KEEP_ALIVE_SECONDS = 50;

    /**
     * 线程的名字前缀
     */
    private  static final String threadNamePreFix = "Async-service-";

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        executor.setThreadNamePrefix(threadNamePreFix);
        //线程池对拒绝任务（无线程可用）的处理策略：由调用线程（提交任务的线程）处理任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化
        executor.initialize();
        return executor;
    }
}
