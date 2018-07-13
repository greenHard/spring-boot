package com.example.advance.condition.config;

import com.example.advance.condition.LinuxCondition;
import com.example.advance.condition.WindowsCondition;
import com.example.advance.condition.service.ListService;
import com.example.advance.condition.service.impl.WindowListServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.condition.config.ConditionConfig
 * @Description: 条件配置类
 * @create 2018/07/10 13:25
 */
@Configuration
public class ConditionConfig {

    @Bean
    @Conditional(WindowsCondition.class)
    public ListService windowsListServiceImpl(){
        return new WindowListServiceImpl();
    }

    @Bean
    @Conditional(LinuxCondition.class)
    public ListService linuxListServiceImpl(){
        return new WindowListServiceImpl();
    }

}
