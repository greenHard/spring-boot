package com.example.advance.condition;

import com.example.advance.condition.config.ConditionConfig;
import com.example.advance.condition.service.ListService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.condition.Main
 * @Description: 运行类
 * @create 2018/07/10 13:27
 */
public class Main {
    public static void main(String[] args) {
        // 1. 获取容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConditionConfig.class);

        // 2.获取接口
        ListService listService = context.getBean(ListService.class);

        // 3. 输出
        System.out.println(context.getEnvironment().getProperty("os.name") + "系统下的列表命令为: " +listService.showListCmd());
    }
}
