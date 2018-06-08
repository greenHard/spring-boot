package com.example.SpringBootWeb.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

/**
 * 自定义ServletRequest监听器
 */
@WebListener
public class MyServletRequestListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        ServletContext servletContext = sre.getServletContext();
        servletContext.log("request is initialized....");
    }


    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        ServletContext servletContext = sre.getServletContext();
        servletContext.log("request is destroyed....");
    }


}
