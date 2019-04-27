package com.example.advance.schedule;

import com.example.advance.schedule.config.TaskScheduleConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.schedule.Main
 * @Description: 执行类
 * @create 2018/07/04 17:22
 */
public class Main {
    public static void main(String[] args) {
        // 1. 获取上下文容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TaskScheduleConfig.class);
    }
}
