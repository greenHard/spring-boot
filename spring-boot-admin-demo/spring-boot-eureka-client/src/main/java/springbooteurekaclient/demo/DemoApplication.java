package springbooteurekaclient.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/** 
 * Eureka客户端启动类, 配置{@link EnableDiscoveryClient}启动客户端
 * @author yuyang.zhang 
 * @date 2019/1/9 
 */ 
@EnableDiscoveryClient
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

