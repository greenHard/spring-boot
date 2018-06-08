package com.segmentdefault.classloader.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class JDBCTestServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -196953652264283668L;
	
	
	private DataSource datasource;
	 
	@Override
	public void init() throws ServletException {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			String bean = (String) envCtx.lookup("Bean");
			System.out.println(bean);
			datasource = (DataSource)
					  envCtx.lookup("jdbc/TestDB");
		}catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter writer = res.getWriter();
		res.setContentType("text/html;charset=UTF-8");
		
		try {
			Connection conn = datasource.getConnection();
			
			Statement stat = conn.createStatement();
			
			ResultSet rs = stat.executeQuery("SHOW DATABASES;");
			
			while(rs.next()) {
				String databaseName = rs.getString(1);
				writer.write(databaseName);
				writer.write("<br/>");
				writer.flush();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
