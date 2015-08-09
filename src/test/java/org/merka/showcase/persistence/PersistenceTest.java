package org.merka.showcase.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.merka.showcase.StartupManager;
import org.merka.showcase.entity.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // this requires jUnit 4.9 or higher
@ContextConfiguration(locations = {"classpath:/spring/test-persistence-context.xml"})
public class PersistenceTest implements InitializingBean
{
	private static EntityManagerFactory entityManagerFactory;
	
	@Autowired
	StartupManager startupManager;
	
	public StartupManager getStartupManager()
	{
		return startupManager;
	}

	public void setStartupManager(StartupManager startupManager)
	{
		this.startupManager = startupManager;
	}

	@Override
	public void afterPropertiesSet()
	{
		startupManager.initDataBase();
		entityManagerFactory = Persistence.createEntityManagerFactory("org.merka.showcase.jpa");		
	}
	
	@Test
	public void testPersisteUser()
	{
		EntityManager manager = entityManagerFactory.createEntityManager();
		
		User testUser = new User();
		testUser.setUsername("test");
		
		manager.getTransaction().begin();
		manager.persist(testUser);
		manager.getTransaction().commit();
		
		assertEquals(1L, testUser.getId());
		
		// retrieves the persisted user with the manager
		User found = manager.find(User.class, testUser.getId());
		assertEquals(testUser.getUsername(), found.getUsername());
		assertEquals(testUser.getId(), found.getId());
		assertNotNull(found.getRanks());
		assertTrue(found.getRanks().isEmpty());
		
		// deletes the user:
		manager.getTransaction().begin();
		manager.remove(found);
		manager.getTransaction().commit();
		// verifies the deletion
		found = manager.find(User.class, testUser.getId());
		assertNull(found);
	}
}
