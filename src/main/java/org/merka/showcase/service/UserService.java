package org.merka.showcase.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.merka.showcase.entity.Rank;
import org.merka.showcase.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService extends BaseService
{
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long	serialVersionUID	= 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
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
	
	public User findUserWithRanksById(Long id)
	{
		EntityManager manager = null;
		try
		{
			manager = getEntityManager();
			User user = manager.find(User.class, id);
			user.getRanks().size();
			return user;
		}
		catch(RuntimeException re)
		{
			logger.error("error during ");
			manager.getTransaction().rollback();
			throw re;
		}
		finally
		{
			manager.close();
		}
	}

	public User save(User user)
	{
		if(user.getId() != null)
		{
			return update(user);
		}
		else
		{
			return insert(user);
		}
	}
	
	protected User insert(User user)
	{
		EntityManager manager = null;
		try
		{
			manager = getEntityManager();
			manager.getTransaction().begin();
			manager.persist(user);
			for (Rank rank : user.getRanks())
			{
				manager.persist(rank);
			}
			manager.getTransaction().commit();
		}
		finally
		{
			if(manager != null)
			{
				manager.close();
			}
		}
		return user;
	}
	
	protected User update(User user)
	{
		EntityManager manager = null;
		try
		{
			getEntityManager();
			manager.getTransaction().begin();
			User mergedUser = manager.merge(user);
			for (Rank rank : mergedUser.getRanks())
			{
				manager.merge(rank);
			}
			manager.getTransaction().commit();
			return mergedUser;
		}
		finally
		{
			if(manager != null)
			{
				manager.close();
			}
		}
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
		EntityManager manager = null;
		try
		{
			manager = getEntityManager();
			manager.getTransaction().begin();

			User upToDate = manager.find(User.class, user.getId());

			manager.remove(upToDate);
			manager.getTransaction().commit();
		} 
		finally 
		{
			if(manager != null)
			{
				manager.close();
			}
		}
	}
}
