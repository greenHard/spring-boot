package com.example.springconfig;

import com.example.springconfig.config.MyEventConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springconfig.Main
 * @Description: 测试类
 * @create 2018/07/04 13:43
 */
public class Main {
    public static void main(String[] args) {
        // 1. 获取上下文容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyEventConfig.class);

        // 2. 获取事件发布类
        MyPublisher publisher = context.getBean(MyPublisher.class);

        // 3. 发布事件
        publisher.publish("hello application event!");

        // 4. 关闭容器
        context.close();
    }
}
