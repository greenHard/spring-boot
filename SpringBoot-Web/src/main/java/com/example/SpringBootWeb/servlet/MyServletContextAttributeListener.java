package com.example.SpringBootWeb.servlet;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.annotation.WebListener;
import java.util.Optional;

/**
 * 自定义ServletContextAttribute监听器
 */
@WebListener
public class MyServletContextAttributeListener implements ServletContextAttributeListener {
    @Override
    public void attributeAdded(ServletContextAttributeEvent scae) {
        Optional.of(scae.getName()+",ServletContext attribute  added...").ifPresent(System.out::println);
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent scae) {
        Optional.of(scae.getName()+",ServletContext attribute  removed...").ifPresent(System.out::println);
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent scae) {
        Optional.of(scae.getName()+",ServletContext attribute  replaced...").ifPresent(System.out::println);
    }
}
