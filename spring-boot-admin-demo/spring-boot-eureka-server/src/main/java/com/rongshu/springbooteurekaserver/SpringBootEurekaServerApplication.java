package com.rongshu.springbooteurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka 服务端启动类, 添加{@link EnableEurekaServer}注解,启动服务
 * @author yuyang.zhang
 * @date 2019/1/9
 */
@EnableEurekaServer
@SpringBootApplication
public class SpringBootEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootEurekaServerApplication.class, args);
    }

}

