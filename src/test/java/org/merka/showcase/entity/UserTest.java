package org.merka.showcase.entity;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

	@Test
	public void testAddRole() 
	{
		User user = User.create("alb");
		user.setPassword("fake");
		user.setEnabled(true);
		
		user.addRole("USER_ADMIN");
		
		assertNotNull(user.getRoles());
		assertTrue(! user.getRoles().isEmpty());
		assertEquals(2, user.getRoles().size());
		
		UserRole role = user.getRoles().get(0);
		assertEquals("ROLE_USER", role.getRole());
	}

}
