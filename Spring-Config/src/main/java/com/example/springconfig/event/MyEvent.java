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
