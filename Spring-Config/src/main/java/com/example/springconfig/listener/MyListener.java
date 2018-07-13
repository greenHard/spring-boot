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
