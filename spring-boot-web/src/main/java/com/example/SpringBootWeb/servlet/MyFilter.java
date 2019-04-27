package com.example.SpringBootWeb.servlet;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注解的方式注册Filter
 */
@WebFilter(
        filterName = "myFilter",
        servletNames = {"myServlet", "myServlet2"}
        //urlPatterns = {"/myServlet","/myServlet3"}
)
public class MyFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ServletContext servletContext = request.getServletContext();
        servletContext.log("/myservlet was filtered!");
        filterChain.doFilter(request, response);
    }
}
