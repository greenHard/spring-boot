package com.example.advance.schedule.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.schedule.config.TaskScheduleConfig
 * @Description: 定时任务配置类
 * @create 2018/07/04 17:21
 */
@Configuration
@EnableScheduling
@ComponentScan("com.example")
public class TaskScheduleConfig {
}
