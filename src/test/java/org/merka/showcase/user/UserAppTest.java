package org.merka.showcase.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContextEvent;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.merka.showcase.entity.User;
import org.merka.showcase.filter.SessionUserInjectorFilter;
import org.merka.showcase.listener.HsqlDBStarterListener;
import org.merka.showcase.listener.StartupManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { 
		"classpath:/spring/root-test-context.xml"
		, "classpath:/spring/servlet-context-test.xml"
		, "classpath:/spring/spring-security-test.xml"
		, "classpath:/spring/test-persistence-context.xml"})
public class UserAppTest implements InitializingBean{
	
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@Autowired
	WebApplicationContext wac;
		
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    
    @Autowired
    SessionUserInjectorFilter sessionUserInjectorFilter;
    
    @Autowired 
    StartupManager startupManager;
    
	MockMvc mockMvc;
	
	@BeforeClass
	public static void beforeClass()
	{
		HsqlDBStarterListener listener = new HsqlDBStarterListener();
		MockServletContext context = new MockServletContext();
		context.addInitParameter("persistence.unit.database.name", "showcase-test");
		ServletContextEvent sce = new ServletContextEvent(context);
		listener.contextInitialized(sce);
	}
	
	@Before
	public void setup()
	{
		
	}
		
	@Test
	public void testAccessProtectedResources() throws Exception 
	{
		mockMvc.perform(get("/welcome")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/loginPage"));
	}
	
	@Test
	public void testLogin() throws Exception
	{
		mockMvc.perform(get("/")).andExpect(status().isOk());
		
		mockMvc.perform(get("/welcome").with(user("rospo").password("rospo").roles("USER")))
			.andExpect(status().isOk());
	}

	@Test
	public void testUserInjectionInSession() throws Exception
	{
		mockMvc.perform(get("/user/ranks").with(user("rospo"))).andExpect(status().isOk());
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.addFilters(springSecurityFilterChain, sessionUserInjectorFilter)
				.build();
		
		EntityManager manager = entityManagerFactory.createEntityManager();
		List<User> users = manager.createQuery("select u from User u", User.class).getResultList();
		if(users.isEmpty()){
			startupManager.setupORM();
		}
	}
}
