package org.merka.showcase.entity;

import java.util.List;

public class Rank
{
	private long id;
	private String name;
	private User owner;
	private List<RankItem> items;
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public User getOwner()
	{
		return owner;
	}
	public void setOwner(User owner)
	{
		this.owner = owner;
	}
	public List<RankItem> getItems()
	{
		return items;
	}
	public void setItems(List<RankItem> items)
	{
		this.items = items;
	}
	public Rank(long id, String name, User owner, List<RankItem> items)
	{
		super();
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.items = items;
	}
	
	
}
