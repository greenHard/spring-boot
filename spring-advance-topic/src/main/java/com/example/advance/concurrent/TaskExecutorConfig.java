package com.example.advance.concurrent;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.concurrent.TaskExecutorConfig
 * @Description: 多线程任务执行配置类
 * @create 2018/07/04 16:44
 */
@Configuration
@ComponentScan("com.example")
@EnableAsync
public class TaskExecutorConfig implements AsyncConfigurer{

    /**
     * 获取异步的执行器
     * @return 返回执行器
     */
    @Override
    public Executor getAsyncExecutor() {
        // 获取一个线程池
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 设置线程池的核心池大小
        taskExecutor.setCorePoolSize(5);
        // 设置线程池最大容量
        taskExecutor.setMaxPoolSize(10);
        // 设置队列等待的最大数量
        taskExecutor.setQueueCapacity(25);
        // 初始化线程池
        taskExecutor.initialize();
        // 返回线程池
        return taskExecutor;
    }
}
