package org.merka.showcase.service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.merka.showcase.listener.HsqlDBStarterListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseServiceTest implements InitializingBean {

	EntityManagerFactory factory;
	
	@Autowired
	HsqlDBStarterListener hsqlStarter;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RankService rankService;
	
	@Override
	public void afterPropertiesSet() throws Exception 
	{
		hsqlStarter.initDataBase();
		factory = Persistence.createEntityManagerFactory("org.merka.showcase.test.jpa");
		userService.setEntityManagerFactory(factory);
		rankService.setEntityManagerFactory(factory);
	}

}
