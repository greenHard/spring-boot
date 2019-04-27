package com.example.SpringBootWeb;

import com.example.SpringBootWeb.spring.boot.MyFilter2;
import com.example.SpringBootWeb.spring.boot.MyServlet3;
import com.example.SpringBootWeb.spring.boot.MyServletContextListener2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.*;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.example.SpringBootWeb.servlet")
public class SpringBootWebApplication {

	public static void main(String[] args) {
        SpringApplication.run(SpringBootWebApplication.class, args);
    }

    /**
     * Spring Boot API 注入Servlet 的方式
     */
	@Bean
    public static ServletRegistrationBean servletRegistrationBean(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new MyServlet3());
        servletRegistrationBean.setName("myServlet3");
        servletRegistrationBean.addUrlMappings("/spring-boot/myServlet3");
        servletRegistrationBean.addInitParameter("myName","zhangyuyang");
        return servletRegistrationBean;
    }

    /**
     * Spring Boot API 注入Filter 的方式
     */
    @Bean
    public static FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new MyFilter2());
        // filterRegistrationBean.addServletNames("myServlet3");
        filterRegistrationBean.addUrlPatterns("/spring-boot/myServlet3");
        return filterRegistrationBean;
    }

    /**
     * Spring Boot API 注入Listener的方式
     */
    @Bean
    public static ServletListenerRegistrationBean servletListenerRegistrationBean(){
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(new MyServletContextListener2());
        return servletListenerRegistrationBean;
    }

}
