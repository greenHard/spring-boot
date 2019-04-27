# Spring常用配置

## 事件(Application Event)

### 1. 自定义事件

```java
package com.example.springconfig.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springconfig.event.MyEvent
 * @Description: 自定义事件
 * @create 2018/07/04 13:34
 */
public class MyEvent extends ApplicationEvent{

    private  String msg;

    public MyEvent(Object source,String msg) {
        super(source);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
```

### 2. 自定义事件监听器

```java
package com.example.springconfig.listener;

import com.example.springconfig.event.MyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springconfig.listener.MyListener
 * @Description: 自定义监听器
 * @create 2018/07/04 13:36
 */
@Component
public class MyListener implements ApplicationListener<MyEvent>{

    @Override
    public void onApplicationEvent(MyEvent myEvent) {
        String msg = myEvent.getMsg();
        System.out.println("我 bean - myListener 接收到了bean- myPublisher发布的消息"+msg);
    }
}
```

### 3. 事件发布类

```java
package com.example.springconfig;

import com.example.springconfig.event.MyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springconfig.MyPublisher
 * @Description: 事件发布类
 * @create 2018/07/04 13:39
 */
@Component
public class MyPublisher {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 发布消息
     * @param msg 消息
     */
    public void publish(String msg) {
        applicationContext.publishEvent(new MyEvent(this, msg));
    }
}
```

#### 3.1 配置类

```java
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
```

#### 3.2 运行

```java
package com.example.springconfig;

import com.example.springconfig.config.MyEventConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhang yuyang
 * @ClassName: com.example.springconfig.Main
 * @Description: 测试类
 * @create 2018/07/04 13:43
 */
public class Main {
    public static void main(String[] args) {
        // 1. 获取上下文容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyEventConfig.class);

        // 2. 获取事件发布类
        MyPublisher publisher = context.getBean(MyPublisher.class);

        // 3. 发布事件
        publisher.publish("hello application event!");

        // 4. 关闭容器
        context.close();
    }
}
```

#### 3.3 测试结果

```ba
13:50:49.809 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Returning cached instance of singleton bean 'lifecycleProcessor'
13:50:49.823 [main] DEBUG org.springframework.core.env.PropertySourcesPropertyResolver - Could not find key 'spring.liveBeansView.mbeanDomain' in any property source
13:50:49.830 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Returning cached instance of singleton bean 'myPublisher'
13:50:49.831 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Returning cached instance of singleton bean 'myListener'
我 bean - myListener 接收到了bean- myPublisher发布的消息hello application event!
13:50:49.832 [main] INFO org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@28f67ac7: startup date [Wed Jul 04 13:50:49 CST 2018]; root of context hierarchy
13:50:49.833 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Returning cached instance of singleton bean 'lifecycleProcessor'
13:50:49.834 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Destroying singletons in org.springframework.beans.factory.support.DefaultListableBeanFactory@7fac631b: defining beans [org.springframework.context.annotation.internalConfigurationAnnotationProcessor,org.springframework.context.annotation.internalAutowiredAnnotationProcessor,org.springframework.context.annotation.internalRequiredAnnotationProcessor,org.springframework.context.annotation.internalCommonAnnotationProcessor,org.springframework.context.event.internalEventListenerProcessor,org.springframework.context.event.internalEventListenerFactory,myEventConfig,myListener,myPublisher]; root of factory hierarchy
```