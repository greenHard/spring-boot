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
