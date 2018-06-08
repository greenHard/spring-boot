package com.segmentdefault.classloader.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
	ClassLoader classLoader = sce.getServletContext().getClassLoader();
		
		while (true) {

			System.out.println(classLoader.getClass().getName());
			classLoader = classLoader.getParent();

			if (classLoader == null) {
				break;
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	

	}

	

}
