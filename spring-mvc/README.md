# Spring MVC基础

###  1. 日志配置(logback.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>

<!-- scan="true"    当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true。 -->
<!--  scanPeriod="30 seconds"   设置每30秒自动扫描,若没有指定具体单位则以milliseconds为标准(单位:milliseconds, seconds, minutes or hours)  -->
<!-- debug="false"当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。-->
<configuration scan="true" scanPeriod="60 seconds" debug="false">

    <!-- 上下文名称  -->
    <contextName>logback</contextName>

    <!-- 存放日志文件路径 -->
    <property name="Log_Home" value="/app/huajifen/log/resource/${SERVER_FLAG}/"/>

    <!-- ch.qos.logback.core.ConsoleAppender 控制台输出 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <!--格式化输出：%d表示日期，%thread表示线程名，%highlight 高亮显示,%M:%L 显示方法名和行号,%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符
                具体详情见: https://logback.qos.ch/manual/layouts.html -->
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %highlight(%-5level) %cyan(%logger{50}) %highlight({%M:%L}) - %highlight(%msg%n)</pattern>
        </encoder>
    </appender>

    <!-- INFO级别 -->
    <appender name="FILE_INFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 级别过滤器 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <!-- 设置过滤级别 -->
            <level>INFO</level>
            <!-- 用于配置符合过滤条件的操作 -->
            <onMatch>ACCEPT</onMatch>
            <!-- 用于配置不符合过滤条件的操作 -->
            <onMismatch>DENY</onMismatch>
        </filter>
        <Encoding>UTF-8</Encoding>

        <!-- 根据时间来制定滚动策略 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <Prudent>true</Prudent>
            <!-- 每个小时生成一个文件  %d{yyyy-MM-dd-HH} DatePattern-->
            <FileNamePattern>
                ${Log_Home}/%d{yyyy-MM-dd-HH}.log
            </FileNamePattern>
        </rollingPolicy>

        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>
                %d{yyyy-MM-dd HH:mm:ss} -%msg%n
            </Pattern>
        </layout>

        <encoder>
            <!--格式化输出：%d表示日期，%thread表示线程名，%highlight 高亮显示,%M:%L 显示方法名和行号,%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符
                具体详情见: https://logback.qos.ch/manual/layouts.html -->
            <Pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %highlight(%-5level) %cyan(%logger{50}) %highlight({%M:%L}) - %highlight(%msg%n)</Pattern>
        </encoder>
    </appender>

    <!-- 控制java下面包的打印,没设置等级,将继承上级root的等级 -->
    <logger name="com.rongshu"/>

    <!-- 将springframework.web包下的类日志级别设置为DEBUG,出现4XX错误,设置将看到更多的信息-->
    <logger name="org.springframework.web" level="DEBUG"/>

    <!-- 当前日志总级别为TRACE、DEBUG、INFO、 WARN、ERROR、ALL和 OF -->
    <!-- the level of the root level is set to DEBUG by default.       -->
    <root level="INFO">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="FILE_INFO"/>
    </root>

</configuration>
```

> ① 将org.springframework.web 包下的日志级别设置为DEBUG, 我们开发Spring MVC 经常出现和参数类型有关的4XX错误, 设置此项我们能看到更多的错误信息



### 2. Spring MVC配置

```java
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
```

> `@EnableWebMvc`注解会开启一些默认配置,如一些`ViewReslover`或者`MessageConverter`等

###  3. Web配置

```java
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
```

> ① `WebApplicationInitalizer` 是`Spring`提供用来配置 `Servlet` 3.0+配置的接口,从而实现了替代`web.xml`的配置。实现该接口将会自动被`SpringServletContainerInitalizer`(用来启动`Servlet` 3.0 容器)获取到
>
> ② 新建 `WebApplicationContext`,注册配置类,并将其和`ServletContext`关联
>
> ③ 注册`Spring MVC` 的` DispatcherServlet`



### 4. 简单控制器

```java
package com.example.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springmvc.controller.HelloController
 * @Description: 控制器
 * @create 2018/07/12 10:31
 */
@Controller
public class HelloController {

    @RequestMapping("/index")
    public String hello(){
        return "index";
    }
}
```

### 5. 常用注解

@Controller、@RequestMapping、@ResponseBody、@RequestBody、@PathVariable、@RestController

### 6. 注解演示控制器

```java
package com.example.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springmvc.controller.DemoAnnoController
 * @Description: 注解演示控制器
 * @create 2018/07/12 10:51
 */
@Controller
@RequestMapping("/anno")
public class DemoAnnoController {

    /**
     * 此方法未标明路径,因此使用类级别的路径/anno;
     * produces可定制返回的媒体类型和字符集,若需返回json对象,
     * 则设置produces = "application/json;charset=UTF-8"
     * 演示可接受HttpServletRequest作为参数,当然也可以接受
     * HttpServletResponse作为参数。此处的ResponseBody用在返回值前面
     */
    @RequestMapping(produces = "text/plain;charset=UTF-8")
    public @ResponseBody
    String index(HttpServletRequest request) {
        return "url: " + request.getRequestURI() + " can access";
    }

    /**
     * 演示接受路径参数,并在方法参数前结合@PathVariable 使用,
     * 访问路径为/anno/pathvar/{str}
     */
    @RequestMapping(value = "/pathvar/{str}", produces = "text/plain;charset=UTF-8")
    public @ResponseBody
    String demoPathVar(@PathVariable String str, HttpServletRequest request) {
        return "url: " + request.getRequestURI() + " can access,str: " + str;
    }


    /**
     * 演示常规的request参数获取,访问路径为/anno/requestParam?id=1,
     */
    @RequestMapping(value = "/requestParam", produces = "text/plain;charset=UTF-8")
    public @ResponseBody
    String passRequestParam(Long id, HttpServletRequest request) {
        return "url: " + request.getRequestURI() + " can access,id: " + id;
    }

    /**
     * 演示接受参数到对象,访问路径为/anno/obj?name=xx
     * {@link ResponseBody }也可以用在方法上
     */
    @RequestMapping(value = "/obj", produces = "application/json;charset=UTF-8") // 7
    @ResponseBody
    public String passObj(DemoObj obj, HttpServletRequest request) {
        return "url: " + request.getRequestURI() + " can access, obj name : " + obj.getName();
    }

    /**
     * 演示不同路径到相同的方法。
     * 访问路径为/anno/name1 或 /anno/name2
     */
    @RequestMapping(value = {"/name1", "/name2"}, produces = "text/plain;charset=UTF-8") //9
    public @ResponseBody
    String remove(HttpServletRequest request) {
        return "url: " + request.getRequestURI() + " can access";
    }
}

```

###  7. RestController演示

```java
package com.example.springmvc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springmvc.controller.DemoRestController
 * {@link RestController}
 * {@link ResponseBody}
 * @Description: 演示，使用RestController,返回数据时不需要ResponseBody
 * @create 2018/07/12 11:15
 */
@RestController
@RequestMapping("/rest")
public class DemoRestController {

    /**
     * 返回数据的媒体类型为json
     * 直接返回对象,对象会在自动转换为json
     */
    @RequestMapping(value = "/getJson", produces = {"application/json;charset=UTF-8"})
    public DemoObj getJson(DemoObj obj) {
        return new DemoObj(obj.getName() + "dd");
    }

    /**
     * 返回数据的媒体类型为xml
     * 直接返回对象,对象会在自动转换为xml
     */
    @RequestMapping(value = "/getXml", produces = {"application/xml;charset=UTF-8"})
    public DemoObj getXml(DemoObj obj) {
        return new DemoObj(obj.getName() + "dd");
    }
}
```

