package com.example.advance.meta.bean;

import org.springframework.stereotype.Service;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.meta.bean.DemoService
 * @Description: demo bean
 * @create 2018/07/10 15:21
 */
@Service
public class DemoService {
    public void outputResult(){
        System.out.println("从组合注解配置照样获得的bean");
    }
}
