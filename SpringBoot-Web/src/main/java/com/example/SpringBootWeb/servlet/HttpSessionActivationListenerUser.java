package com.example.SpringBootWeb.servlet;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;
import java.io.Serializable;
import java.util.Optional;

/**
@Description:
    实现了HttpSessionActivationListener接口的 JavaBean 对象可以感知自己被活化和钝化的事件
    活化:javabean对象和Session一起被反序列化(活化)到内存中。
    钝化:javabean对象存在Session中，当服务器把session序列化到硬盘上时，如果Session中的javabean对象实现了Serializable接口
    那么服务器会把session中的javabean对象一起序列化到硬盘上，javabean对象和Session一起被序列化到硬盘中的这个操作称之为钝化
    如果Session中的javabean对象没有实现Serializable接口，那么服务器会先把Session中没有实现Serializable接口的javabean对象移除
    然后再把Session序列化(钝化)到硬盘中
    当绑定到 HttpSession对象中的javabean对象将要随 HttpSession对象被钝化之前，
    web服务器调用该javabean对象对象的 void sessionWillPassivate(HttpSessionEvent event)方法
    这样javabean对象就可以知道自己将要和 HttpSession对象一起被序列化(钝化)到硬盘中
    当绑定到HttpSession对象中的javabean对象将要随 HttpSession对象被活化之后，
    web服务器调用该javabean对象的 void sessionDidActive(HttpSessionEvent event)方法
    这样javabean对象就可以知道自己将要和 HttpSession对象一起被反序列化(活化)回到内存中
 *
 */
public class HttpSessionActivationListenerUser implements Serializable,HttpSessionActivationListener{

    private String name;

    private Integer age;

    private String address;

    @Override
    public void sessionWillPassivate(HttpSessionEvent se) {
        Optional.of(name+"和session一起被序列化(钝化)到硬盘了，session的id是："+se.getSession().getId()).ifPresent(System.out::println);
    }

    @Override
    public void sessionDidActivate(HttpSessionEvent se) {
        Optional.of(name+"和session一起从硬盘反序列化(活化)回到内存了，session的id是："+se.getSession().getId()).ifPresent(System.out::println);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
