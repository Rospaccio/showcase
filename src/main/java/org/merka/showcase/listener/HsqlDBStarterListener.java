package org.merka.showcase.listener;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl.AclFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HsqlDBStarterListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(HsqlDBStarterListener.class);
	
	private static Server inMemoryServer;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		initDataBase();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (inMemoryServer != null) {
			inMemoryServer.shutdown();
		}
	}
	
	public void initDataBase() {
		try {
			String tableCreationStatement = "DROP TABLE IF EXISTS USER CASCADE; DROP TABLE IF EXISTS USER_ROLES;"
					// -----
					+ "CREATE TABLE USER "
					+ "(USERNAME VARCHAR(45) NOT NULL"
					+ ", PASSWORD VARCHAR(60) NOT NULL"
					+ ", ENABLED BOOLEAN NOT NULL"
					+ ", PRIMARY KEY (USERNAME));"
					+ "CREATE TABLE user_roles ("
					+ "  user_role_id int NOT NULL identity"
					+ ",  username varchar(45) NOT NULL"
					+ ",  role varchar(45) NOT NULL"
					+ ",  UNIQUE (role,username)"
					+ ",  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES user (username));";

			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
			String password1 = encoder.encode("rospo");
			String password2 = encoder.encode("rospo2");

			String insertStatement = "insert into USER values ('rospo', '"
					+ password1 + "', true);";
			String insertStatement2 = "insert into USER values ('rospo2', '"
					+ password2 + "', true);";

			String insertStatement3 = "INSERT INTO user_roles (username, role) VALUES ('rospo2', 'ROLE_USER'); "
					+ "INSERT INTO user_roles (username, role) VALUES ('rospo', 'ROLE_ADMIN');"
					+ "INSERT INTO user_roles (username, role) VALUES ('rospo', 'ROLE_USER');";

			HsqlProperties p = new HsqlProperties();
			p.setProperty("server.database.0", "file:showcase");
			p.setProperty("server.dbname.0", "showcase");
			p.setProperty("server.port", "5222");

			inMemoryServer = new Server();
			inMemoryServer.setProperties(p);
			inMemoryServer.start();

			// Connection c = DriverManager.getConnection(
			// "jdbc:hsqldb:hsql://localhost:5222/showcase", "sa", "");
			// Statement statement = c.createStatement();
			// statement.execute(tableCreationStatement);
			// statement.execute(insertStatement);
			// statement.execute(insertStatement2);
			// statement.execute(insertStatement3);
		} catch (/* SQLException | */IOException | AclFormatException e) {
			logger.error("Impossible to start the in-memory database", e);
			throw new RuntimeException(e);
		}
	}

}
