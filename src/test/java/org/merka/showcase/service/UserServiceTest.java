package org.merka.showcase.service;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.jpa.internal.util.PersistenceUtilHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.merka.showcase.entity.User;
import org.merka.showcase.listener.HsqlDBStarterListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-persistence-context.xml"})
public class UserServiceTest implements InitializingBean{

	EntityManagerFactory factory;
	
	@Autowired
	HsqlDBStarterListener hsqlStarter;
	
	@Autowired
	UserService userService;
	
	@Test
	public void testSaveUser() 
	{
		User user = User.create("test-user");
		userService.save(user);
		
		assertNotNull(user.getId());
		
		EntityManager manager = factory.createEntityManager();
		User found = manager.find(User.class, user.getId());
		
		assertNotNull(found);
		assertEquals(found.getUsername(), user.getUsername());
		assertEquals(found.getPassword(), user.getPassword());
		assertEquals(found.getId(), user.getId());
		
		manager.close();
	}

	@Override
	public void afterPropertiesSet() throws Exception 
	{
		hsqlStarter.initDataBase();
		factory = Persistence.createEntityManagerFactory("org.merka.showcase.test.jpa");
		userService.setEntityManagerFactory(factory);
	}
}
