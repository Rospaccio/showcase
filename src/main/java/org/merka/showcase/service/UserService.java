package org.merka.showcase.service;

import javax.persistence.EntityManager;

import org.merka.showcase.entity.Rank;
import org.merka.showcase.entity.User;

public class UserService extends BaseService
{
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long	serialVersionUID	= 1L;

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
			return manager.createQuery("select u from User u where u.username = :username", User.class)
					.setParameter("username", username).getSingleResult();
		}
		finally
		{
			manager.close();
		}
	}
	
	public void delete(User user)
	{
		EntityManager manager =  getEntityManager();
		manager.getTransaction().begin();
		
		User upToDate = manager.find(User.class, user.getId());
		
		manager.remove(upToDate);
		manager.getTransaction().commit();
		manager.close();
	}
}
