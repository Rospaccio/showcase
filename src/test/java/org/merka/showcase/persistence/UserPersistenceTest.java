package org.merka.showcase.persistence;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.merka.showcase.entity.Rank;
import org.merka.showcase.entity.User;
import org.merka.showcase.entity.UserRole;
import org.merka.showcase.listener.HsqlDBStarterListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/test-persistence-context.xml" })
public class UserPersistenceTest implements InitializingBean {

	static EntityManagerFactory factory;

	@Autowired
	HsqlDBStarterListener hsqlStarter;
	
//	@BeforeClass
//	public static void staticSetup() {
//		hsqlStarter.initDataBase();
//		factory = Persistence
//				.createEntityManagerFactory("org.merka.showcase.test.jpa");
//	}

	@Override
	public void afterPropertiesSet()
	{
		hsqlStarter.initDataBase();
		factory = Persistence.createEntityManagerFactory("org.merka.showcase.test.jpa");		
	}
	
	@AfterClass
	public static void shutdown() {
		if (factory != null) {
			factory.close();
		}
	}

	@Test
	public void testSaveWithRelationships() {
		EntityManager manager = newManager();
		
		User user = User.create("testUser");
		user.addRole(UserRole.ROLE_ADMIN);
		user.addRole(UserRole.ROLE_USER);
		
		manager.getTransaction().begin();
		manager.persist(user);
		manager.getTransaction().commit();
		manager.close();
		
		manager = newManager();
		User found1 = manager.find(User.class, user.getId());
		Rank rank = Rank.create("my Rank", "test rank");
		found1.addRank(rank);
		manager.getTransaction().begin();
		//manager.persist(found1);
		manager.persist(rank);
		manager.getTransaction().commit();
		manager.close();
		
		manager = newManager();
		User found2 = manager.find(User.class, user.getId());
		assertNotNull(found2);
		assertNotNull(found2.getRanks());
		assertTrue(! found2.getRanks().isEmpty());
		assertEquals(1, found2.getRanks().size());
		assertEquals("my Rank", found2.getRanks().get(0).getName());
		
		
		
		manager.close();
	}

	private EntityManager newManager()
	{
		return factory.createEntityManager();
	}
}
