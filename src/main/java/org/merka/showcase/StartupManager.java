package org.merka.showcase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl.AclFormatException;
import org.merka.showcase.entity.Rank;
import org.merka.showcase.entity.User;
import org.merka.showcase.entity.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class StartupManager implements ServletContextListener
{

	private static final Logger	logger	= LoggerFactory.getLogger(StartupManager.class);

	private static Server		inMemoryServer;

	private static EntityManagerFactory entityManagerFactory;
	
	public StartupManager()
	{
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		if (inMemoryServer != null)
		{
			inMemoryServer.shutdown();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		logger.info("\n\n\n inside contextInitialized! \n\n\n");
		initDataBase();
		setupORM();
	}

	public void initDataBase()
	{
		try
		{
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
			
			String insertStatement = "insert into USER values ('rospo', '" + password1 + "', true);";
			String insertStatement2 = "insert into USER values ('rospo2', '" + password2 + "', true);";

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

//			Connection c = DriverManager.getConnection(
//					"jdbc:hsqldb:hsql://localhost:5222/showcase", "sa", "");
//			Statement statement = c.createStatement();
//			statement.execute(tableCreationStatement);
//			statement.execute(insertStatement);
//			statement.execute(insertStatement2);
//			statement.execute(insertStatement3);
		}
		catch (/*SQLException |*/ IOException | AclFormatException e)
		{
			logger.error("Impossible to start the in-memory database", e);
			throw new RuntimeException(e);
		}
	}
	
	public static EntityManagerFactory getEntityManagerFactory(){
		return entityManagerFactory;
	}
	
	public void setupORM()
	{
		// The parameter of this method call (persistenceUnitName) must match
		// the one set in persistence.xml
		// as the value of the value attribute of persistence-unit
		entityManagerFactory = Persistence
				.createEntityManagerFactory("org.merka.showcase.jpa");
		try
		{
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
			// adds the default test users
			User devUser = User.create("rospo");
			devUser.setPassword(encoder.encode("rospo"));
			
			User devUser2 = User.create("rospo2");
			devUser2.setPassword(encoder.encode("rospo2"));
			
			EntityManager manager = entityManagerFactory.createEntityManager();
			manager.getTransaction().begin();
			manager.persist(devUser);
			manager.persist(devUser2);
			
			for(int i = 0; i < 3; i++)
			{
				Rank r = Rank.create("Rank #" + i, "A Test rank");
				r.setOwner(devUser);
				manager.persist(r);
			}
			manager.getTransaction().commit();
		}
		catch (Exception e)
		{
			logger.error("Error while adding new app User", e);
		}
	}

}
