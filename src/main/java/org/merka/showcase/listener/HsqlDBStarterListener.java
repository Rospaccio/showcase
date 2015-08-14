package org.merka.showcase.listener;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl.AclFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class HsqlDBStarterListener implements ServletContextListener {

	public static final String PERSISTENCE_UNIT_DATABASE_NAME = "persistence.unit.database.name";

	private static final Logger logger = LoggerFactory.getLogger(HsqlDBStarterListener.class);
	
	private static Server inMemoryServer;
	
	@Value("${persistence.unit.database.name}")
	String databaseName;
	
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("\n\n\n");
		logger.info("=============================");
		logger.info("Initializing servlet context");
		logger.info("=============================\n\n");
		if(StringUtils.isBlank(databaseName))
		{
			configureDBNameFromInitParams(sce.getServletContext());
		}
		initDataBase();
	}
	
	private void configureDBNameFromInitParams(ServletContext context) {
		String dbName = context.getInitParameter(PERSISTENCE_UNIT_DATABASE_NAME);
		databaseName = dbName;
		assertDatabaseName();
	}

	private void assertDatabaseName()
	{
		if(StringUtils.isEmpty(databaseName)){
			throw new IllegalStateException("init parameter \"persistence.unit.database.name\" is empty: it must"
					+ "be set as a context-param in web.xml if this object is initialized as a context listener,"
					+ " or as a configuration property if this object is"
					+ "managed as a spring bean (e.g. in unit tests)");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (inMemoryServer != null) {
			inMemoryServer.shutdown();
		}
	}
	
	public void initDataBase() {
		logger.info("\n\n\n");
		logger.info("=============================");
		logger.info("Initializing database");
		logger.info("=============================\n\n");
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

}
