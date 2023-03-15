package cn.rmy.common.config;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.Task;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定时任务线程池
 *
 * @author chu
 * @date 2022/04/01
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    /**
     * Callback allowing a {@link TaskScheduler
     * TaskScheduler} and specific {@link Task Task}
     * instances to be registered against the given the {@link ScheduledTaskRegistrar}.
     *
     * @param taskRegistrar the registrar to be configured.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
        taskScheduler.setPoolSize(10);
        taskRegistrar.setScheduler(taskScheduler);
    }
}
