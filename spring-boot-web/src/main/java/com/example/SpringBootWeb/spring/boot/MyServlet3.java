package com.example.SpringBootWeb.spring.boot;

import com.example.SpringBootWeb.servlet.HttpSessionBindingListenerUser;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 非注解的方式注册Servlet
 */
public class MyServlet3 extends HttpServlet {

    private String myName;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myName = config.getInitParameter("myName");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getServletContext();
        PrintWriter writer = resp.getWriter();
        writer.write("<html><body>hello,"+myName+" !</body></html>");
    }
}