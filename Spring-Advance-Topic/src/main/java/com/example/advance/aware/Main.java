package com.example.advance.aware;

import com.example.advance.aware.config.AwareConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.aware.Main
 * @Description: 测试类
 * @create 2018/07/04 15:22
 */
public class Main {
    public static void main(String[] args){
        // 1. 获取上下文容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AwareConfig.class);

        // 2.获取bean
        AwareService awareService = context.getBean(AwareService.class);

        // 3. 输出文件内容
        awareService.outPutResult();

        // 4. 关闭容器
        context.close();

    }
}
