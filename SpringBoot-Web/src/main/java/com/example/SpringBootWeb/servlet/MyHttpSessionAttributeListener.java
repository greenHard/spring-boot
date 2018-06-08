package com.example.SpringBootWeb.servlet;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.Optional;

/**
 * 自定义SessionAttributeListener监听器
 */
@WebListener
public class MyHttpSessionAttributeListener implements HttpSessionAttributeListener {
    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        Optional.of("session id is "+se.getSession().getId()+", session attribute is added").ifPresent(System.out::println);
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {
        Optional.of("session id is "+se.getSession().getId()+", session attribute is removed").ifPresent(System.out::println);
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
        Optional.of("session id is "+se.getSession().getId()+", session attribute is replaced").ifPresent(System.out::println);
    }
}
