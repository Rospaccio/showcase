package org.merka.showcase.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.merka.showcase.entity.Rank;
import org.merka.showcase.entity.User;

public class UserService extends User
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	EntityManagerFactory		entityManagerFactory;

	public EntityManagerFactory getEntityManagerFactory()
	{
		return entityManagerFactory;
	}

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory)
	{
		this.entityManagerFactory = entityManagerFactory;
	}

	public UserService()
	{
	}

	public User findUserById(Long userId)
	{
		EntityManager manager = getEntityManager();
		try
		{
			return manager.find(User.class, userId);
		}
		finally
		{
			manager.close();
		}
	}

	public User save(User user)
	{
		EntityManager manager = getEntityManager();
		manager.getTransaction().begin();
		manager.persist(user);
		for (Rank rank : user.getRanks())
		{
			manager.persist(rank);
		}
		manager.getTransaction().commit();
		manager.close();

		return user;
	}

	public User findUserByUsername(String username)
	{
		EntityManager manager = getEntityManager();
		try
		{
			return manager.createQuery("select u from User u where u.username = :username", User.class).setParameter("username", username).getSingleResult();
		}
		finally
		{
			manager.close();
		}
	}

	private EntityManager getEntityManager()
	{
		return entityManagerFactory.createEntityManager();
	}
}
