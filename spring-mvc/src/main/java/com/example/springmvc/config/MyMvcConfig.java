package com.example.springmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springmvc.config.MyMvcConfig
 * @Description: springmvc 配置
 * @create 2018/07/10 17:00
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.example")
public class MyMvcConfig {

    @Bean
    public InternalResourceViewResolver viewResolver(){
        // 1. 创建内部视图解析器
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        // 2. 设置前缀
        viewResolver.setPrefix("/WEV-INF/classes/views/");
        // 3. 设置后缀
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(JstlView.class);
        return viewResolver;
    }
}
