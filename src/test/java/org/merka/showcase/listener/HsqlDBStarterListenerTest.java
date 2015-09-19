package org.merka.showcase.listener;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

public class HsqlDBStarterListenerTest {

	@Test
	public void testContextInitialized()
	{
		HsqlDBStarterListener listener = new HsqlDBStarterListener();
		MockServletContext context = new MockServletContext();
		context.addInitParameter(HsqlDBStarterListener.PERSISTENCE_UNIT_DATABASE_NAME, "showcase-test");
		ServletContextEvent event = new ServletContextEvent(context);
		listener.contextInitialized(event);

		listener.initDataBase();

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("org.merka.showcase.test.jpa");
		assertNotNull(factory);
		EntityManager manager = factory.createEntityManager();
		assertNotNull(manager);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testFailingContextInitialized() 
	{
		HsqlDBStarterListener probe = new HsqlDBStarterListener();
		MockServletContext context = new MockServletContext();
		ServletContextEvent event = new ServletContextEvent(context);
		probe.contextInitialized(event);
	}
	
	@Test(expected = IllegalStateException.class)
	@Ignore
	public void testFailingInitDB(){
		HsqlDBStarterListener hsqldInit = new HsqlDBStarterListener();
		hsqldInit.initDataBase();
	}

}
