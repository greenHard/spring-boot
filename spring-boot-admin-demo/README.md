# Spring Boot Admin

## 1. 什么是Spring Boot Admin

`Spring Boot Admin` 是一个社区的项目用来监控和管理我们的`Spring Boot` 应用。应用注册可以通过  `Spring Boot Admin Client (Http)` 或者使用 `Spring Cloud` 注册中心去发现（例如`Eureka`, `Consul`)。

## 2. 配置 Spring Boot Admin Server

### 2.1 配置pom.xml

```xml
 <properties>
        <java.version>1.8</java.version>
        <spring-boot-admin.version>2.1.1</spring-boot-admin.version>
    </properties>

    <dependencies>
        <!-- 服务端：带UI界面 -->
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 邮件提醒 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>

        <!-- 权限配置-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>de.codecentric</groupId>
                <artifactId>spring-boot-admin-dependencies</artifactId>
                <version>${spring-boot-admin.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```



### 2.2 配置启动项

```java
package com.rongshu.springbootadminserver;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 服务端配置
 * @author yuyang.zhang
 * @date 2019/1/1
 */
@SpringBootApplication
@EnableAdminServer
public class SpringBootAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdminServerApplication.class, args);
    }

}
```

> 添加 `@EnableAdminServer`启动注解

### 2.3 安全配置

```java
package com.rongshu.springbootadminserver.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * 安全权限配置
 *
 * @author <p>yuyang.zhang<p>
 * @date 2018-12-01 13:50
 * @since 1.0
 */
@Configuration
public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

    private final String adminContextPath;

    public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        http.authorizeRequests()
                 // 授予公共访问所有静态资产和登录页面
                .antMatchers(adminContextPath + "/assets/**").permitAll()
                .antMatchers(adminContextPath + "/login").permitAll()
                // 其他必须经过身份验证的请求
                .anyRequest().authenticated()
                .and()
                // 配置登录和注销
                .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
                .logout().logoutUrl(adminContextPath + "/logout").and()
                // 使http基本支持。这是spring的引导管理客户机所需的登记
                .httpBasic().and()
                // 使csrf保护使用cookie
                .csrf()
                // 禁用CRSF-Protection端点弹簧引导管理客户机使用登记
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                // 禁用CRSF-Protection致动器的端点
                .ignoringAntMatchers(
                        adminContextPath + "/instances",
                        adminContextPath + "/actuator/**"
                );
        // @formatter:on
    }
}
```

### 2.4 配置application.properties

```properties
# 配置启动端口
server.port=8080

# 邮件配置
spring.mail.host = smtp.163.com
spring.mail.username=m17612184394@163.com
spring.mail.password=pwd

# admin 发送配置
spring.boot.admin.notify.mail.from=m17612184394@163.com
spring.boot.admin.notify.mail.to=2544267857@qq.com

# security用户名密码
spring.security.user.name=admin
spring.security.user.password=123456
```

### 2.4 自定义提醒

```java
package com.rongshu.springbootadminserver.config;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import de.codecentric.boot.admin.server.notify.LoggingNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * 自定义Notifier
 * @author <p>yuyang.zhang<p>
 * @date 2019-01-03 18:39
 * @since 1.0
 */
public class CustomNotifier  extends AbstractEventNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingNotifier.class);

    public CustomNotifier(InstanceRepository repository) {
        super(repository);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent) {
                LOGGER.info("Instance {} ({}) is {}", instance.getRegistration().getName(), event.getInstance(),
                        ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus());
            } else {
                LOGGER.info("Instance {} ({}) {}", instance.getRegistration().getName(), event.getInstance(),
                        event.getType());
            }
        });
    }
}
```

> 在#doNotify() 可以实现自己的逻辑。

### 2.5 启动项目

#### 2.5.1 登录界面

http://localhost:8080/

![](.\image\1.png)

> 输入`application.properties` 配置的账号和密码

#### 2.5.2 主页

![](.\image\2.png)



## 3. 注册  Client Applications

### 3.1 Spring Boot Admin Client

使用 `Spring Boot Admin Client` 进行注册

#### 3.1.1 配置pom.xml

```xml

    <properties>
        <java.version>1.8</java.version>
        <spring-boot-admin.version>2.1.1</spring-boot-admin.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-client</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- JMX management -->
        <dependency>
            <groupId>org.jolokia</groupId>
            <artifactId>jolokia-core</artifactId>
        </dependency>

    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>de.codecentric</groupId>
                <artifactId>spring-boot-admin-dependencies</artifactId>
                <version>${spring-boot-admin.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

#### 3.1.2 安全配置

```java
package com.rongshu.springbootadminclient.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全权限配置
 * @author <p>yuyang.zhang<p>
 * @date 2018-12-01 13:50
 * @since 1.0
 */
@Configuration
public class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .csrf()
                .disable();
    }
}
```

#### 3.1.3 配置application.properties

```properties
# 配置端口号
server.port=8081

# 客户端注册地址
spring.boot.admin.client.url=http://localhost:8080

# ip 地址,默认使用主机名
spring.boot.admin.client.instance.prefer-ip=true

# 配置用户名和密码
spring.boot.admin.client.username=admin
spring.boot.admin.client.password=123456

# 端点暴露,这个很重要,必须要
management.endpoints.web.exposure.include=*

# 日志配置
logging.file=/var/log/sample-boot-application.log
logging.pattern.file=%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(${PID}){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n%wEx

# tags
#using the metadata
spring.boot.admin.client.instance.metadata.tags.environment=test
#using the info endpoint
info.tags.environment=test
```

#### 3.1.4 启动项目

![](.\image\3.png)

> Server 端 登录页面已经发生改变, 有一个应用已经成功注册。



- 主页展示

![](.\image\4.png)



- 日志展示

  ![](.\image\\6.png)

> 日志和数据都是实时监控,动态改变的。

### 3.2 Spring Cloud Discovery

#### 3.2.1 Eureka Server端

##### 3.2.1.1 配置pom.xml

```xml
  <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.7.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.rongshu</groupId>
    <artifactId>spring-boot-eureka-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-boot-eureka-server</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Finchley.SR2</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

##### 3.2.1.2 配置启动注解

```java
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
```

##### 3.2.1.3 配置 application.properties

```properties
# 应用名称
spring.application.name=eureka-server
# 端口
server.port=1001
# 主机名
eureka.instance.hostname=localhost
# 取消eureka自我注册功能
eureka.client.register-with-eureka=false
# 不需要检索服务
eureka.client.fetch-registry=false
```

##### 3.2.1.4 启动项目

![](.\image\7.png)

#### 3.2.2 Eureka Client端

##### 3.2.2.1 配置pom.xml

```xml
  <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.7.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>spring-boot-eureka-client</groupId>
    <artifactId>demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>demo</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Finchley.SR2</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- JMX management -->
        <dependency>
            <groupId>org.jolokia</groupId>
            <artifactId>jolokia-core</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

##### 3.2.2.2 配置启动注解

```java
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

```

##### 3.2.2.3 配置application.properties

```properties
# 应用名称
spring.application.name=eureka-client
# 客户端端口
server.port=2001
# 注册地址
eureka.client.serviceUrl.defaultZone=http://localhost:1001/eureka/

#表示将ip注册到Eureka server
eureka.instance.prefer-ip-address=true

# 将Instance ID设置成IP:端口的形式
eureka.instance.instance-id=${spring.cloud.client.ip-address}:${server.port}

# 端点暴露,这个很重要,必须要
management.endpoints.web.exposure.include=*
```

> 在SpringBoot2.0 之后,不支持驼峰命名的方式`spring.cloud.client.ip-address`一定要这么写。

 ##### 3.2.2.4 启动项目

![](.\image\8.png)

> 查看Eureka发现有Eureka客户端注册上去了

#### 3.2.3 Spring Boot Admin Server 端

##### 3.2.3.1 配置pom.xml

```xml
  <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.7.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.rongshu</groupId>
    <artifactId>spring-boot-admin-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-boot-admin-server</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-boot-admin.version>2.0.4</spring-boot-admin.version>
        <spring-cloud.version>Finchley.SR2</spring-cloud.version>
    </properties>

    <dependencies>
        <!-- 服务端：带UI界面 -->
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 邮件提醒 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>

        <!-- 权限配置-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>de.codecentric</groupId>
                <artifactId>spring-boot-admin-dependencies</artifactId>
                <version>${spring-boot-admin.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-dependencies</artifactId>
                    <version>${spring-cloud.version}</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

##### 3.2.3.2 配置启动注解

```java
/**
 * 服务端配置
 * @author yuyang.zhang
 * @date 2019/1/1
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class SpringBootAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdminServerApplication.class, args);
    }

}

```

##### 3.2.3.3 配置application.properties

```pro
# 配置启动端口
server.port=8080

# 邮件配置
spring.mail.host = smtp.163.com
spring.mail.username=m17612184394@163.com
spring.mail.password=Zyy15922313068

# admin 发送配置
spring.boot.admin.notify.mail.from=m17612184394@163.com
spring.boot.admin.notify.mail.to=2544267857@qq.com

# security用户名密码
spring.security.user.name=admin
spring.security.user.password=123456

# 应用名称
spring.application.name=spring-admin-server
# eureka注册地址
eureka.client.serviceUrl.defaultZone=http://localhost:1001/eureka/
# 将Instance ID设置成IP:端口的形式
eureka.instance.instance-id=${spring.cloud.client.ip-address}:${server.port}
#表示将ip注册到Eureka server
eureka.instance.prefer-ip-address=true
```

##### 3.2.3.4 启动项目

http://localhost:1001/  

![](.\image\9.png)

http://localhost:8080/login

![](.\image\10.png)

> 登录后发现Eureka的客户端实例,已经可以被springBootAdmin监控了。



## 参考资料

https://codecentric.github.io/spring-boot-admin/2.0.4/

https://docs.spring.io/spring-boot/docs/2.0.7.RELEASE/reference/htmlsingle/

http://blog.didispace.com/spring-cloud-starter-dalston-1/