package org.merka.showcase.persistence;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.merka.showcase.entity.Rank;
import org.merka.showcase.entity.User;
import org.merka.showcase.entity.UserRole;
import org.merka.showcase.service.BaseServiceTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-persistence-context.xml"})
public class DetachedObjectsTest extends BaseServiceTest {

	@Test
	public void testSaveParentEntity() 
	{
		User testUser = User.create("testUser");
		testUser.setPassword("password");
		EntityManager firstManager = entityFactoryManager.createEntityManager();
		firstManager.getTransaction().begin();
		firstManager.persist(testUser);
		firstManager.getTransaction().commit();
		firstManager.close();
		firstManager = null;
		
		assertNotNull(testUser.getId());
		
		testUser.setUsername("modified");
		testUser.setPassword("modified");
		testUser.addRole(UserRole.ROLE_ADMIN);
		
		EntityManager secondManager = entityFactoryManager.createEntityManager();
		secondManager.getTransaction().begin();
		secondManager.persist(testUser.getRoles().get(1));
		User merged = secondManager.merge(testUser);
		secondManager.getTransaction().commit();
		secondManager.close();
		
		EntityManager thirdManager = entityFactoryManager.createEntityManager();
		User found = thirdManager.find(User.class, testUser.getId());
		assertEquals("modified", found.getUsername());
		assertEquals("modified", found.getPassword());
		assertEquals(2, found.getRoles().size());
		
		thirdManager.remove(found);
		thirdManager.close();
	}

	@Test
	public void testDeleteChildEntity()
	{
		User user = User.create("test");
		user.setPassword("test");
		Rank delenda = Rank.create("rank", "rank");
		user.addRank(delenda);
		
		EntityManager manager = entityFactoryManager.createEntityManager();
		manager.getTransaction().begin();
		manager.persist(user);
		manager.getTransaction().commit();
		
		User forConfirmation = manager.find(User.class, user.getId());
		manager.close();
		assertEquals(1, forConfirmation.getRanks().size());
		
		// creates another manager (new session) and deletes the rank entity
		EntityManager secondManager = entityFactoryManager.createEntityManager();
		secondManager.getTransaction().begin();
		Rank attached = secondManager.find(Rank.class, delenda.getId());
		secondManager.remove(attached);
		secondManager.getTransaction().commit();
		secondManager.close();
		
		EntityManager thirdManager = entityFactoryManager.createEntityManager();
		thirdManager.getTransaction().begin();
		User found = thirdManager.find(User.class, user.getId());
		assertEquals(0, found.getRanks().size());
		thirdManager.remove(found);		
		thirdManager.getTransaction().commit();
		thirdManager.close();
	}
}
