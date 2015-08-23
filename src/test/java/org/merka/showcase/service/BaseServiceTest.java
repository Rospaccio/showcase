package org.merka.showcase.service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.merka.showcase.listener.HsqlDBStarterListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseServiceTest implements InitializingBean {

	@Autowired
	protected EntityManagerFactory entityFactoryManager;
	
	@Autowired
	protected HsqlDBStarterListener hsqlStarter;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected RankService rankService;
	
	@Override
	public void afterPropertiesSet() throws Exception 
	{
//		hsqlStarter.initDataBase();
//		factory = Persistence.createEntityManagerFactory("org.merka.showcase.test.jpa");
//		userService.setEntityManagerFactory(factory);
//		rankService.setEntityManagerFactory(factory);
	}

}
