package org.merka.showcase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl.AclFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartupManager implements ServletContextListener
{

	private static final Logger logger = LoggerFactory.getLogger(StartupManager.class);
	
	public StartupManager()
	{
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		logger.info("\n\n\n inside contextInitialized! \n\n\n");
		try
		{
			String tableCreationStatement = "CREATE TABLE USER (USERNAME VARCHAR(45) NOT NULL, PRIMARY KEY (USERNAME));";
			
			HsqlProperties p = new HsqlProperties();
	        p.setProperty("server.database.0", "file:showcase");
	        p.setProperty("server.dbname.0", "showcase");
	        p.setProperty("server.port", "5222");
			
			Server hsqlServer = new Server();
			hsqlServer.setProperties(p);
			hsqlServer.start();
			
			Connection c = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:5222/showcase", "sa", "");
			Statement statement = c.createStatement();
			statement.execute(tableCreationStatement);
		}
		catch (SQLException | IOException | AclFormatException e)
		{
			logger.error("Impossible to start the in-memory database", e);
			throw new RuntimeException(e);
		}
	}

}
