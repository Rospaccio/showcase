package org.merka.showcase.user;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.merka.showcase.listener.HsqlDBStarterListener;
import org.merka.showcase.listener.StartupManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
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
	HsqlDBStarterListener hsqlStarter;
	
	@Autowired
	StartupManager startupManager;
	
	@Autowired
	WebApplicationContext wac;
		
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    
	MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(springSecurityFilterChain).build();
	}
		
	@Test
	public void testAccessProtectedResources() throws Exception 
	{
		mockMvc.perform(get("/welcome")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/loginPage"));
	}
	
	@Test
	public void testLogin() throws Exception
	{
		mockMvc.perform(post("/login")).andExpect(status().is4xxClientError());
		
//		mockMvc.perform(post("/login")
//				.param("username", "rospo")
//				.param("password", "rospo"))
//				.andExpect(status().isOk());
	}

	@Override
	public void afterPropertiesSet() throws Exception 
	{
		hsqlStarter.initDataBase();
		startupManager.setupORM();
	}

}
