package com.example.springmvc.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springmvc.config.WebInitializer
 * @Description: Web配置
 * @create 2018/07/12 10:23
 */
public class WebInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // 1. 创建Web容器
        AnnotationConfigWebApplicationContext ctx= new AnnotationConfigWebApplicationContext();

        // 2.注册配置和上下文
        ctx.register(MyMvcConfig.class);
        ctx.setServletContext(servletContext);

        // 3. 注册Spring MVC的DispatcherServlet
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatch", new DispatcherServlet(ctx));
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);
    }
}
