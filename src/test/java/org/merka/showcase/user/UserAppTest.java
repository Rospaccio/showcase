package org.merka.showcase.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.merka.showcase.StartupManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration("src/test/resources")
@ContextConfiguration(locations = { 
		"classpath:/spring/root-test-context.xml"
		, "classpath:/spring/servlet-context-test.xml"
		, "classpath:/spring/spring-security-test.xml" 
		, "classpath:/spring/test-persistence-context.xml"})
public class UserAppTest implements InitializingBean {

	@Autowired
	StartupManager startupManager;
	
	@Autowired
	WebApplicationContext wac;
	
	MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
		
	@Test
	public void testAccessProtectedResources() throws Exception 
	{
		mockMvc.perform(get("/")).andExpect(status().isOk());
	}

	@Override
	public void afterPropertiesSet() throws Exception 
	{
		startupManager.initDataBase();
		startupManager.setupORM();
	}

}
