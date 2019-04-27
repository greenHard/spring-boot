# Spring 高级话题

## 1. Spring Aware

### 1.1 介绍

`Spring Aware` 的目的就是让`Bean`获得`Spring`容器的服务。因为`ApplicationContext`接口集成了`MessageSource`接口、`ApplicationEventPublisher`接口和`ResourceLoader`接口,所以`ApplicationContextAware`可以获得`Spring`容器的所有服务,但原则上我们还是用到什么接口,就实现什么接口。

### 1.2 示例

#### 1.2.1 准备

在classpath下面建一个`test.txt`,内容随意，给下面的外部资源加载使用。

####  1.2.2 Spring Aware演示Bean

```java
package com.example.advance.aware;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.aware.AwareService
 * @Description: spring aware服务类, 实现bean
 * {@link BeanNameAware}
 * {@link ResourceLoaderAware}
 * @create 2018/07/04 14:18
 */
@Component
public class AwareService implements BeanNameAware,ResourceLoaderAware {

    private String beanName;

    private ResourceLoader loader;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.loader = resourceLoader;
    }

    public void outPutResult(){
        // 输出类的名称
        System.out.println("Bean 的名称为: "+beanName);

        // 加载文件
        Resource resource = loader.getResource("classpath:test.txt");

        // 输出文件内容
        try {
            System.out.println(IOUtils.toString(resource.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

```

#### 1.2.3 代码解释

> ① 实现`BeanNameAware`,`ResourceLoaderAware`接口，获得`Bean`名称和资源加载的服务。
> ② 实现`ResourceLoaderAware`需要重写`setResourceLoader`方法。
> ③ 实现`BeanNameAware`需要重写`setBeanName`方法。

#### 1.2.4 配置类

```java
package com.example.advance.aware.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.aware.config.AwareConfig
 * @Description: aware配置类
 * @create 2018/07/04 15:21
 */
@Configuration
@ComponentScan("com.example")
public class AwareConfig {
}
```

#### 1.2.5 运行

```java
package com.example.advance.aware;

import com.example.advance.aware.config.AwareConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.aware.Main
 * @Description: 测试类
 * @create 2018/07/04 15:22
 */
public class Main {
    public static void main(String[] args){
        // 1. 获取上下文容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AwareConfig.class);

        // 2.获取bean
        AwareService awareService = context.getBean(AwareService.class);

        // 3. 输出文件内容
        awareService.outPutResult();

        // 4. 关闭容器
        context.close();

    }
}
```

**运行结果如下**

```
15:28:12.483 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Returning cached instance of singleton bean 'lifecycleProcessor'
15:28:12.488 [main] DEBUG org.springframework.core.env.PropertySourcesPropertyResolver - Could not find key 'spring.liveBeansView.mbeanDomain' in any property source
15:28:12.492 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Returning cached instance of singleton bean 'awareService'
Bean 的名称为: awareService
test
name=bill
```

## 2. 多线程

### 2.1 介绍

`Spring` 通过任务执行器（`TaskExecutor`）来实现多线程和并发编程。使用`ThreadPoolTaskExecutor`可实现一个基于线程池的`TaskExecutor`。实际开发中任务一般是非阻碍的,即异步的, 所以我们要在配置类中通过`@EnableAsync`开启对异步任务的支持,并通过实际执行的`Bean`的方法中使用`@Async`注解来声明其是一个异步任务。

### 2.2 示例

#### 2.2.1 配置类

```java
package com.example.advance.concurrent;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.concurrent.TaskExecutorConfig
 * @Description: 多线程任务执行配置类
 * @create 2018/07/04 16:44
 */
@Configuration
@ComponentScan("com.example")
@EnableAsync
public class TaskExecutorConfig implements AsyncConfigurer{

    /**
     * 获取异步的执行器
     * @return 返回执行器
     */
    @Override
    public Executor getAsyncExecutor() {
        // 获取一个线程池
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 设置线程池的核心池大小
        taskExecutor.setCorePoolSize(5);
        // 设置线程池最大容量
        taskExecutor.setMaxPoolSize(10);
        // 设置队列等待的最大数量
        taskExecutor.setQueueCapacity(25);
        // 初始化线程池
        taskExecutor.initialize();
        // 返回线程池
        return taskExecutor;
    }
}
```

#### 2.2.2 代码解释

> ① 利用`@EnableAsync`注解开启异步任务支持
>
> ② 配置类实现`AsyncConfigure`接口并重写`getAsyncExecutor`方法,并返回一个`ThreadPoolTaskExecutor`,这样可以获得一个基于线程池的`TaskExecutor`

#### 2.2.3 任务执行类

```java
package com.example.advance.concurrent.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.concurrent.service.AsyncTaskService
 * @Description: 异步任务执行类
 * @create 2018/07/04 16:53
 */
@Service
public class AsyncTaskService {

    @Async
    public void executeAsyncTask(Integer i) {
        System.out.println("执行异步任务: " + i);
    }

    public void executeAsyncTaskPlus(Integer i) {
        System.out.println("执行异步任务+1： " + (i + 1));
    }
}
```

> 通过`@Async`注解表明该方法是一个异步方法,如果注解在类级别,表明该类所有的方法都是异步方法,而这里的方法自动被注入使用`ThreadPoolTaskExecutor`作为`TaskExecutor`

#### 2.2.4 运行

```java
package com.example.advance.concurrent;

import com.example.advance.concurrent.service.AsyncTaskService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.concurrent.Main
 * @Description: 运行类
 * @create 2018/07/04 16:56
 */
public class Main {
    public static void main(String[] args) {
        // 1. 获取上下文容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TaskExecutorConfig.class);

        // 2.获取bean
        AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);

        // 3. 执行方法
        for (int i = 0; i < 10; i++) {
            asyncTaskService.executeAsyncTask(i);
            asyncTaskService.executeAsyncTaskPlus(i);
        }

        // 4. 关闭容器
        context.close();
    }
}
```

**执行结果如下**

```
17:00:03.178 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Returning cached instance of singleton bean 'asyncTaskService'
执行异步任务+1： 1
执行异步任务: 0
执行异步任务+1： 2
执行异步任务: 1
执行异步任务+1： 3
执行异步任务: 2
执行异步任务+1： 4
执行异步任务+1： 5
执行异步任务+1： 6
执行异步任务+1： 7
执行异步任务+1： 8
执行异步任务+1： 9
执行异步任务+1： 10
17:00:03.199 [main] INFO org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@2a18f23c: startup date [Wed Jul 04 16:59:58 CST 2018]; root of context hierarchy
执行异步任务: 3
执行异步任务: 5
执行异步任务: 6
执行异步任务: 7
执行异步任务: 8
执行异步任务: 9
```

## 3. 计划任务

### 3.1 介绍

从`Spring `3.1开始,计划任务在`Spring`的实现变得异常的简单。首先通过在配置类注解`@EnableScheduling`来开启计划任务的支持, 然后在要执行计划任务的方法上注解`@Schedule`，这是声明一个计划任务。

`Spring`通过`@Schedule` 支持多种类型的计划任务,包含cron、fixDelay、fixRate等

### 3.2 示例

#### 3.2.1 计划任务执行类

```java
package com.example.advance.concurrent;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.concurrent.TaskExecutorConfig
 * @Description: 多线程任务执行配置类
 * @create 2018/07/04 16:44
 */
@Configuration
@ComponentScan("com.example")
@EnableAsync
public class TaskExecutorConfig implements AsyncConfigurer{

    /**
     * 获取异步的执行器
     * @return 返回执行器
     */
    @Override
    public Executor getAsyncExecutor() {
        // 获取一个线程池
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 设置线程池的核心池大小
        taskExecutor.setCorePoolSize(5);
        // 设置线程池最大容量
        taskExecutor.setMaxPoolSize(10);
        // 设置队列等待的最大数量
        taskExecutor.setQueueCapacity(25);
        // 初始化线程池
        taskExecutor.initialize();
        // 返回线程池
        return taskExecutor;
    }
}
```

#### 3.2.2 代码解释

> ① 通过`@Schedule`声明该方法是计划任务,使用fixedRate属性每隔固定时间执行
>
> ② 使用cron属性可以按照指定时间执行,本例指的是每天11点28分执行;cron是UNIX和类UNIX(Linux)系统下的定时任务

 #### 3.2.3 配置类

```java
package com.example.advance.schedule.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.schedule.config.TaskScheduleConfig
 * @Description: 定时任务配置类
 * @create 2018/07/04 17:21
 */
@Configuration
@EnableScheduling
@ComponentScan("com.example")
public class TaskScheduleConfig {
}
```

#### 3.2.4 代码解释

> ① 通过`@EnableScheduling`注解开启对计划任务的支持。

#### 3.2.5 运行

```java
package com.example.advance.schedule;

import com.example.advance.schedule.config.TaskScheduleConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.schedule.Main
 * @Description: 执行类
 * @create 2018/07/04 17:22
 */
public class Main {
    public static void main(String[] args) {
        // 1. 获取上下文容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TaskScheduleConfig.class);
    }
}
```

## 4. 条件注解@Conditional

### 4.1 介绍

通过活动的`profile`,我们可以获得不同的`Bean`。`Spring4`提供了一个更通用的基于条件的`Bean`的创建,即使用`@Conditional`注解。

`@Conditional`根据满足的条件去创建一个特定的`Bean`,比方说,当某一个jar包在一个类路径下的时候,自动配置一个或者多个`Bean`。总体来说,就是根据特定条件来控制`Bean`的创建行为,这样我们可以用这个特性运行一些自动的配置

### 4.2 示例

#### 4.2.1 判断条件定义

- 判断windows的条件

  ```java
  package com.example.advance.condition;
  ss
  import org.springframework.context.annotation.Condition;
  import org.springframework.context.annotation.ConditionContext;
  import org.springframework.core.type.AnnotatedTypeMetadata;
  
  /**
   * @author zhang yuyang
   * @ClassName: com.example.advance.condition.WindowsCondition
   * @Description: 判断条件类 window
   * @create 2018/07/06 14:44
      */
    public class WindowsCondition implements Condition {
  
      @Override
      public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
          return context.getEnvironment().getProperty("os.name").contains("Windows");
      }
  }
  ```

- 判断Linux的条件

  ```java
  package com.example.advance.condition;
  
  import org.springframework.context.annotation.Condition;
  import org.springframework.context.annotation.ConditionContext;
  import org.springframework.core.type.AnnotatedTypeMetadata;
  
  /**
   * @author zhang yuyang
   * @ClassName: com.example.advance.condition.LinuxCondition
   * @Description: 判断条件 Linux
   * @create 2018/07/06 14:47
   */
  public class LinuxCondition implements Condition{
  
      @Override
      public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
          return context.getEnvironment().getProperty("os.name").contains("Linux");
      }
  }
  ```


#### 4.2.2 不同系统下的Bean类

- 接口

  ```java
  package com.example.advance.condition.service;
  
  public interface ListService {
  
      String showListCmd();
  }
  ```

- Windows下需要创建的Bean类

  ```java
  package com.example.advance.condition.service.impl;
  
  import com.example.advance.condition.service.ListService;
  
  /**
   * @author zhang yuyang
   * @ClassName: com.example.advance.condition.service.impl.WindowListServiceImpl
   * @Description: windows 实现类
   * @create 2018/07/10 13:21
   */
  public class WindowListServiceImpl implements ListService {
      @Override
      public String showListCmd() {
          return "dir";
      }
  }
  ```

- Linux下需要创建的Bean类

  ```java
  package com.example.advance.condition.service.impl;
  
  import com.example.advance.condition.service.ListService;
  
  /**
   * @author zhang yuyang
   * @ClassName: com.example.advance.condition.service.impl.LinuxListServiceImpl
   * @Description: Linux实现类
   * @create 2018/07/10 13:23
   */
  public class LinuxListServiceImpl implements ListService {
      @Override
      public String showListCmd() {
          return "ls";
      }
  }
  
  ```


#### 4.2.3 配置类

```java
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
```

> ① 通过`@Conditional`注解,符合Windows条件则实例化windowsListServiceImpl。
>
> ② 通过`@Conditional`注解,符合Linux条件则实例化linuxListServiceImpl。

#### 4.2.4 运行

```java
package com.example.advance.condition;

import com.example.advance.condition.config.ConditionConfig;
import com.example.advance.condition.service.ListService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhang yuyang
 * @ClassName: com.example.advance.condition.Main
 * @Description: 运行类
 * @create 2018/07/10 13:27
 */
public class Main {
    public static void main(String[] args) {
        // 1. 获取容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConditionConfig.class);

        // 2.获取接口
        ListService listService = context.getBean(ListService.class);

        // 3. 输出
        System.out.println(context.getEnvironment().getProperty("os.name") + "系统下的列表命令为: " +listService.showListCmd());
    }
}
```

**执行结果如下**:

```
13:31:13.059 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Returning cached instance of singleton bean 'lifecycleProcessor'
13:31:13.065 [main] DEBUG org.springframework.core.env.PropertySourcesPropertyResolver - Could not find key 'spring.liveBeansView.mbeanDomain' in any property source
13:31:13.073 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Returning cached instance of singleton bean 'windowsListServiceImpl'
13:31:13.073 [main] DEBUG org.springframework.core.env.PropertySourcesPropertyResolver - Found key 'os.name' in PropertySource 'systemProperties' with value of type String
Windows 7系统下的列表命令为: dir
```

## 5. @Enable*注解的工作原理

### 5.1 介绍

通过简单的@Enable*来开启一项功能的支持,从而避免自己配置大量的代码,大大降低使用难度。

`@EnableWebMvc`、`@EnableAsync`、`@EnableScheduling`、`@EnableConfigurationProperties`等

### 5.2 分类

#### 第一类: 直接导入配置类

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SchedulingConfiguration.class)
@Documented
public @interface EnableScheduling {

}
```

直接导入配置类`SchedulingConfiguration`,这个类注解了`@Configuration`,且注册了一个`scheduledAnnotationProcessor`的Bean

```java
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class SchedulingConfiguration {

	@Bean(name = TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME)
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public ScheduledAnnotationBeanPostProcessor scheduledAnnotationProcessor() {
		return new ScheduledAnnotationBeanPostProcessor();
	}
}
```

#### 第二类: 依据条件选择配置类

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AsyncConfigurationSelector.class)
public @interface EnableAsync {

	/**
	 * Indicate the 'async' annotation type to be detected at either class
	 * or method level.
	 * <p>By default, both Spring's @{@link Async} annotation and the EJB 3.1
	 * {@code @javax.ejb.Asynchronous} annotation will be detected.
	 * <p>This attribute exists so that developers can provide their own
	 * custom annotation type to indicate that a method (or all methods of
	 * a given class) should be invoked asynchronously.
	 */
	Class<? extends Annotation> annotation() default Annotation.class;

	/**
	 * Indicate whether subclass-based (CGLIB) proxies are to be created as opposed
	 * to standard Java interface-based proxies.
	 * <p><strong>Applicable only if the {@link #mode} is set to {@link AdviceMode#PROXY}</strong>.
	 * <p>The default is {@code false}.
	 * <p>Note that setting this attribute to {@code true} will affect <em>all</em>
	 * Spring-managed beans requiring proxying, not just those marked with {@code @Async}.
	 * For example, other beans marked with Spring's {@code @Transactional} annotation
	 * will be upgraded to subclass proxying at the same time. This approach has no
	 * negative impact in practice unless one is explicitly expecting one type of proxy
	 * vs. another &mdash; for example, in tests.
	 */
	boolean proxyTargetClass() default false;

	/**
	 * Indicate how async advice should be applied.
	 * <p><b>The default is {@link AdviceMode#PROXY}.</b>
	 * Please note that proxy mode allows for interception of calls through the proxy
	 * only. Local calls within the same class cannot get intercepted that way; an
	 * {@link Async} annotation on such a method within a local call will be ignored
	 * since Spring's interceptor does not even kick in for such a runtime scenario.
	 * For a more advanced mode of interception, consider switching this to
	 * {@link AdviceMode#ASPECTJ}.
	 */
	AdviceMode mode() default AdviceMode.PROXY;

	/**
	 * Indicate the order in which the {@link AsyncAnnotationBeanPostProcessor}
	 * should be applied.
	 * <p>The default is {@link Ordered#LOWEST_PRECEDENCE} in order to run
	 * after all other post-processors, so that it can add an advisor to
	 * existing proxies rather than double-proxy.
	 */
	int order() default Ordered.LOWEST_PRECEDENCE;

}

```

`AsyncConfigurationSelector`通过条件来选择需要导入的配置类

```java

/**
 * Selects which implementation of {@link AbstractAsyncConfiguration} should be used based
 * on the value of {@link EnableAsync#mode} on the importing {@code @Configuration} class.
 *
 * @author Chris Beams
 * @since 3.1
 * @see EnableAsync
 * @see ProxyAsyncConfiguration
 */
public class AsyncConfigurationSelector extends AdviceModeImportSelector<EnableAsync> {

	private static final String ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME =
			"org.springframework.scheduling.aspectj.AspectJAsyncConfiguration";

	/**
	 * {@inheritDoc}
	 * @return {@link ProxyAsyncConfiguration} or {@code AspectJAsyncConfiguration} for
	 * {@code PROXY} and {@code ASPECTJ} values of {@link EnableAsync#mode()}, respectively
	 */
	@Override
	@Nullable
	public String[] selectImports(AdviceMode adviceMode) {
		switch (adviceMode) {
			case PROXY:
				return new String[] { ProxyAsyncConfiguration.class.getName() };
			case ASPECTJ:
				return new String[] { ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME };
			default:
				return null;
		}
	}
}
```

#### 第三类: 动态注册Bean

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AspectJAutoProxyRegistrar.class)
public @interface EnableAspectJAutoProxy {

	/**
	 * Indicate whether subclass-based (CGLIB) proxies are to be created as opposed
	 * to standard Java interface-based proxies. The default is {@code false}.
	 */
	boolean proxyTargetClass() default false;

	/**
	 * Indicate that the proxy should be exposed by the AOP framework as a {@code ThreadLocal}
	 * for retrieval via the {@link org.springframework.aop.framework.AopContext} class.
	 * Off by default, i.e. no guarantees that {@code AopContext} access will work.
	 * @since 4.3.1
	 */
	boolean exposeProxy() default false;

}

```

`AspectJAutoProxyRegistrar`实现了`ImportBeanDefinitionRegistar` 接口,`ImportBeanDefinitionRefistar`的作用是在运行时自动添加Bean到已有的配置类,通过重写方法:

```java
class AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar {

	/**
	 * Register, escalate, and configure the AspectJ auto proxy creator based on the value
	 * of the @{@link EnableAspectJAutoProxy#proxyTargetClass()} attribute on the importing
	 * {@code @Configuration} class.
	 */
	@Override
	public void registerBeanDefinitions(
			AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

		AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);

		AnnotationAttributes enableAspectJAutoProxy =
				AnnotationConfigUtils.attributesFor(importingClassMetadata, EnableAspectJAutoProxy.class);
		if (enableAspectJAutoProxy != null) {
			if (enableAspectJAutoProxy.getBoolean("proxyTargetClass")) {
				AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
			}
			if (enableAspectJAutoProxy.getBoolean("exposeProxy")) {
				AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
			}
		}
	}
}
```