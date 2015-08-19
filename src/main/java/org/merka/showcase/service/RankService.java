package org.merka.showcase.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.merka.showcase.entity.Rank;

public class RankService extends BaseService{

	public Rank findById(Long id)
	{
		EntityManager manager = getEntityManager();
		Rank rank = manager.find(Rank.class, id);
		return rank;
	}
	
	public List<Rank> findAllByUsername(String username)
	{
		EntityManager manager = getEntityManager();
		List<Rank> rankResults = manager
									.createQuery("select r from Rank r where r.owner.username = :username", Rank.class)
									.setParameter("username", username)
									.getResultList();
		manager.close();
		return rankResults;
	}
	
	public void delete(Rank rank)
	{
		delete(rank.getId());
	}
	
	public void delete(Long rankId)
	{
		EntityManager manager = getEntityManager();
		Rank upToDateRank = manager.find(Rank.class, rankId);
		manager.getTransaction().begin();
		manager.remove(upToDateRank);
		manager.getTransaction().commit();
		manager.close();
	}
}
