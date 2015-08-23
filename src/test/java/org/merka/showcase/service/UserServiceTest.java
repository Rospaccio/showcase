package org.merka.showcase.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.merka.showcase.entity.Rank;
import org.merka.showcase.entity.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-persistence-context.xml"})
public class UserServiceTest extends BaseServiceTest{
	
	@Test
	public void testSaveUser() 
	{
		User user = User.create("test-user");
		userService.save(user);
		
		assertNotNull(user.getId());
		
		EntityManager manager = entityFactoryManager.createEntityManager();
		User found = manager.find(User.class, user.getId());
		
		assertNotNull(found);
		assertEquals(found.getUsername(), user.getUsername());
		assertEquals(found.getPassword(), user.getPassword());
		assertEquals(found.getId(), user.getId());
		
		manager.close();
	}
	
	@Test
	public void testSaveUserRelationships()
	{
		User original = User.create("original");
		original.setPassword("test");
		Rank rank = Rank.create("test", "test");
		original.addRank(rank);
		
		userService.save(original);
		
		EntityManager manager = entityFactoryManager.createEntityManager();
		User saved = manager.find(User.class, original.getId());
		
		assertNotNull(saved);
		assertEquals(saved.getUsername(), original.getUsername());
		assertEquals(saved.getPassword(), original.getPassword());
		assertEquals(saved.getId(), original.getId());
		
		assertNotNull(saved.getRoles());
		assertTrue(! saved.getRoles().isEmpty());
		assertEquals(1, saved.getRoles().size());
		
		assertNotNull(saved.getRanks());
		assertTrue(! saved.getRanks().isEmpty());
		assertEquals(1, saved.getRanks().size());
		
		Rank savedRank = saved.getRanks().get(0);
		assertEquals(rank.getName(), savedRank.getName());
		assertEquals(rank.getDescription(), savedRank.getDescription());
		assertEquals(rank.getId(), savedRank.getId());
	}
	
	@Test
	public void testDelete()
	{
		User toBeDeleted = User.create("toBeDeleted");
		toBeDeleted.setPassword("toBeDeleted");
		Rank rank = Rank.create("toBeDeleted", "toBeDeleted");
		toBeDeleted.addRank(rank);
		
		userService.save(toBeDeleted);
		
		assertNotNull(userService.findUserById(toBeDeleted.getId()));
		
		userService.delete(toBeDeleted);
		
		assertEquals(null, userService.findUserById(toBeDeleted.getId()));
	}
}
