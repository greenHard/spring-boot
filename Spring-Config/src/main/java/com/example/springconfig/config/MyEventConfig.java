package com.example.springconfig.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springconfig.config.MyEventConfig
 * @Description: 我的事件配置类,扫描各个类,使用spring管理
 * @create 2018/07/04 13:41
 */
@Configuration
@ComponentScan(basePackages = "com.example")
public class MyEventConfig {

}
