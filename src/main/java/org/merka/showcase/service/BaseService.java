package org.merka.showcase.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class BaseService {

	EntityManagerFactory		entityManagerFactory;

	public EntityManagerFactory getEntityManagerFactory()
	{
		return entityManagerFactory;
	}

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory)
	{
		this.entityManagerFactory = entityManagerFactory;
	}
	
	protected EntityManager getEntityManager()
	{
		return entityManagerFactory.createEntityManager();
	}
}
