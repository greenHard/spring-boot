package com.example.SpringBootWeb.servlet;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.annotation.WebListener;
import java.util.Optional;

/**
 * 自定义ServletRequestAttributeListener监听器
 */
// @WebListener
public class MyServletRequestAttributeListener implements ServletRequestAttributeListener {
    @Override
    public void attributeAdded(ServletRequestAttributeEvent srae) {
        Optional.of("request attribute is added...").ifPresent(System.out::println);
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent srae) {
        Optional.of("request attribute is removed...").ifPresent(System.out::println);
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent srae) {
        Optional.of("request attribute is replaced...").ifPresent(System.out::println);
    }
}
