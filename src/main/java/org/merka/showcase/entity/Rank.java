package org.merka.showcase.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RANK")
public class Rank
{
	private long id;
	private String name;
	private String description;
	private User owner;
	private List<RankItem> items;
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
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
	
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = User.class, optional = false)
	@JoinColumn(name = "OWNER_ID")
	public User getOwner()
	{
		return owner;
	}
	public void setOwner(User owner)
	{
		this.owner = owner;
	}
	
//	public List<RankItem> getItems()
//	{
//		return items;
//	}
//	public void setItems(List<RankItem> items)
//	{
//		this.items = items;
//	}
	
	public Rank(){
		
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
