package com.example.SpringBootTomcat;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

/**
 * @author zhang yuyang
 * @ClassName: com.example.SpringBootTomcat.CustomizationBean
 * @Description: 自定义Servlet container 容器配置类
 * @create 2018/04/08 17:06
 */
@Component
public class CustomizationBean  implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>{

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        factory.setPort(8888);
        factory.setContextPath("/spring-boot");
    }
}
