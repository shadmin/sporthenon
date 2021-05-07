package com.sporthenon.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sporthenon.db.DatabaseManager;

public class ContextListener implements ServletContextListener {

	private static final Logger log = Logger.getLogger(ContextListener.class.getName());
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		final String dbHost = 	System.getenv("SHDB_HOST");
		final String dbPort = 	System.getenv("SHDB_PORT");
		final String dbName = 	System.getenv("SHDB_NAME");
		final String dbUser = 	System.getenv("SHDB_USER");
		final String dbPwd = 	System.getenv("SHDB_PWD");
		try {
			DatabaseManager.createConnectionPool(dbHost, dbPort, dbName, dbUser, dbPwd);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}