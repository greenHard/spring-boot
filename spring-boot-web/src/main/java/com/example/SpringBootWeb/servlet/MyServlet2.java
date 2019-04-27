package com.example.SpringBootWeb.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 注解的方式注册Servlet
 */
@WebServlet(
        name = "myServlet2",
        urlPatterns = "/myServlet4",
        initParams = {
                @WebInitParam(name = "myName", value = "myValue")
        })
public class MyServlet2 extends HttpServlet {

    private String value;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        value = config.getInitParameter("myName");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getServletContext();
        PrintWriter writer = resp.getWriter();
        servletContext.log("myServlet doGet.....");
        writer.write("<html><body>hello, world ! "+value+" </body></html>");
    }
}