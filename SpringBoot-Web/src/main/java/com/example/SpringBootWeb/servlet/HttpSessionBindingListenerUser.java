package com.example.SpringBootWeb.servlet;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.Optional;

/**
 *  HttpSessionBinding
 *  @Description:
 *  实现了HttpSessionBindingListener接口的 JavaBean对象可以感知自己被绑定到 Session中和从Session中删除的事件
 *  当对象被绑定到 HttpSession 对象中时，web 服务器调用该对象的  void valueBound(HttpSessionBindingEvent event) 方法
 *  当对象从 HttpSession 对象中解除绑定时，web 服务器调用该对象的 void valueUnbound(HttpSessionBindingEvent event)方法
 */
public class HttpSessionBindingListenerUser implements HttpSessionBindingListener {

    private String  name;

    private Integer age;

    public HttpSessionBindingListenerUser(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        Optional.of("session id  is "+event.getSession().getId()+", object is bound").ifPresent(System.out::println);
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        Optional.of("session id  is "+event.getSession().getId()+", object is unbound").ifPresent(System.out::println);
    }
}
