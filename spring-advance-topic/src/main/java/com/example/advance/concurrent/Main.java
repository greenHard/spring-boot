package com.example.advance.concurrent;

import com.example.advance.concurrent.service.AsyncTaskService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.concurrent.Main
 * @Description: 运行类
 * @create 2018/07/04 16:56
 */
public class Main {
    public static void main(String[] args) {
        // 1. 获取上下文容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TaskExecutorConfig.class);

        // 2.获取bean
        AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);

        // 3. 执行方法
        for (int i = 0; i < 10; i++) {
            asyncTaskService.executeAsyncTask(i);
            asyncTaskService.executeAsyncTaskPlus(i);
        }

        // 4. 关闭容器
        context.close();
    }
}
