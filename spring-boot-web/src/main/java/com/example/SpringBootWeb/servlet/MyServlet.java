package com.example.SpringBootWeb.servlet;

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
 * 注解的方式注册Servlet
 */
@WebServlet(
        name = "myServlet",
        urlPatterns = {"/myServlet", "/myServlet2"},
        initParams = {
                @WebInitParam(name = "myName", value = "myValue")
        })
public class MyServlet extends HttpServlet {

    private String value;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        value = config.getInitParameter("myName");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        ServletContext servletContext = req.getServletContext();
        servletContext.setAttribute("name","张三");
        session.setAttribute("age","20");
        HttpSessionBindingListenerUser httpSessionBindingListenerUser = new HttpSessionBindingListenerUser("lisi",20);
        session.setAttribute("user",httpSessionBindingListenerUser);
        PrintWriter writer = resp.getWriter();
        servletContext.log("myServlet doGet.....");
        writer.write("<html><body>hello, world ! "+value+" </body></html>");
        session.setAttribute("age","30");
        session.removeAttribute("age");
        session.removeAttribute("user");
        session.invalidate();
        servletContext.removeAttribute("name");
    }
}