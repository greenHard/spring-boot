package com.example.advance.meta;

import com.example.advance.meta.bean.DemoService;
import com.example.advance.meta.config.DemoConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.meta.Main
 * @Description: 组合注解demo运行类
 * @create 2018/07/10 15:23
 */
public class Main {
    public static void main(String[] args) {
        // 1. 获取容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoConfig.class);

        // 2.获取demoService
        DemoService demoService = context.getBean(DemoService.class);

        // 3. 测试输出
        demoService.outputResult();

        // 4. 关闭容器
        context.close();
    }
}
