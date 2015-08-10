package org.merka.showcase.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "APPUSER")
public class User
{
	private long id;
	private String username;
	private List<Rank> ranks;
	
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
	
	@Column(nullable = false)
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	@OneToMany(mappedBy = "owner")	
	public List<Rank> getRanks()
	{
		return ranks;
	}
	public void setRanks(List<Rank> ranks)
	{
		this.ranks = ranks;
	}
	public User(){
		setRanks(new ArrayList<>());
	}
	
	public User(long id, String username)
	{
		this();
		this.id = id;
		this.username = username;
	}
	
	public static User create(String username){
		User user = new User();
		user.setUsername(username);
		return user;
	}
	
}
