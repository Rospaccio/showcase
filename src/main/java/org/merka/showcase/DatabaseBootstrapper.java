package org.merka.showcase;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl.AclFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseBootstrapper 
{
	private static final Logger logger = LoggerFactory.getLogger(DatabaseBootstrapper.class);
	
	String databaseName;
	private org.hsqldb.server.Server inMemoryServer;
	
	public String getDatabaseName() 
	{
		return databaseName;
	}

	public void setDatabaseName(String databaseName) 
	{
		this.databaseName = databaseName;
	}
	
	public DatabaseBootstrapper()
	{
		logger.info("DatabaseBootstrapper constructor");
	}
	
	@PostConstruct
	public void startDatabaseInstance()
	{
		logger.info("\n\n\n\n");
		logger.info("=============================");
		logger.info("Initializing database");
		logger.info("=============================\n\n\n");
		try {
			if(inMemoryServer != null){
				// already started, nothing to do
				return;
			}

			//precondition: databaseName must be set
			assertDatabaseName();
			
			HsqlProperties p = new HsqlProperties();
			p.setProperty("server.database.0", "file:" + databaseName);
			p.setProperty("server.dbname.0", databaseName);
			p.setProperty("server.port", "5222");

			inMemoryServer = new Server();
			inMemoryServer.setProperties(p);
			inMemoryServer.start();

		} 
		catch (/* SQLException | */IOException | AclFormatException e) 
		{
			logger.error("Impossible to start the in-memory database", e);
			throw new RuntimeException(e);
		}
	}
	
	private void assertDatabaseName()
	{
		if(StringUtils.isEmpty(databaseName)){
			throw new IllegalStateException("Property 'databaseName' in null or empty: cannot start database if I don't know its name");
		}
	}
	
	public void shutdownDatabaseInstance()
	{
		if(inMemoryServer != null)
		{
			inMemoryServer.shutdown();
		}
	}
}
