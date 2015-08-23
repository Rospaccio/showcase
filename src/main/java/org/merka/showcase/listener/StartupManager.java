package org.merka.showcase.listener;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.merka.showcase.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StartupManager implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(StartupManager.class);

	@Autowired
	EntityManagerFactory entityManagerFactory;

	// EntityManagerFactory entityManagerFactory;
	//
	// public EntityManagerFactory getEntityManagerFactory() {
	// return entityManagerFactory;
	// }
	//
	// public void setEntityManagerFactory(EntityManagerFactory
	// entityManagerFactory) {
	// this.entityManagerFactory = entityManagerFactory;
	// }

	public StartupManager() {
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		// EntityManagerFactory factory = WebApplicationContextUtils
		// .getRequiredWebApplicationContext(event.getServletContext())
		// .getAutowireCapableBeanFactory().getBean(EntityManagerFactory.class);
		//
		// entityManagerFactory = factory;

		 insertDefaultData();
	}

	// public static EntityManagerFactory getEntityManagerFactory() {
	// return entityManagerFactory;
	// }

	/**
	 * Populates the database with the default development data
	 */
	public void insertDefaultData() {
		// The parameter of this method call (persistenceUnitName) must match
		// the one set in persistence.xml
		// as the value of the value attribute of persistence-unit
		// entityManagerFactory = Persistence
		// .createEntityManagerFactory("org.merka.showcase.jpa");
		try {
			EntityManager manager = entityManagerFactory.createEntityManager();
			User existingUser = manager.find(User.class, 1L);
			if (existingUser != null) {
				return;
			}

			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
			// adds the default test users
			User devUser = User.create("rospo");
			devUser.setPassword(encoder.encode("rospo"));

			User devUser2 = User.create("test");
			devUser2.setPassword(encoder.encode("test"));

			manager.getTransaction().begin();
			manager.persist(devUser);
			manager.persist(devUser2);
			manager.flush();

			manager.getTransaction().commit();
			manager.close();
		} catch (Exception e) {
			logger.error("Error while adding new app User", e);
		}
	}

}
