package org.merka.showcase.service;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.merka.showcase.entity.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/test-persistence-context.xml" })
public class UserServiceUtilsTest implements InitializingBean
{

	EntityManagerFactory	factory;

	@Autowired
	UserService				userService;

	@Test
	public void testSave()
	{
		User user = User.create("test");
		user.setPassword("test");
		
		UserServiceUtils.save(user);
		
		userService.setEntityManagerFactory(factory);
		
		User found = userService.findUserById(user.getId());
		assertEquals(user.getId(), found.getId());
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		factory = Persistence.createEntityManagerFactory("org.merka.showcase.test.jpa");
	}

}
