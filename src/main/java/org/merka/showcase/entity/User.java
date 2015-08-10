package org.merka.showcase.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "USER")
public class User implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8906683916992569277L;
	
	private long id;
	private String username;
	private String password;
	private boolean enabled;
	private List<UserRole> roles;
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
	
	@Column(nullable = false, unique = true)
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	public List<UserRole> getRoles() {
		return roles;
	}
	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
	}
	
	public User(){
		setRanks(new ArrayList<>());
		setRoles(new ArrayList<UserRole>());
		setEnabled(true);
	}
	
	public User(long id, String username)
	{
		this();
		this.id = id;
		this.username = username;
	}
	
	public void addRole(String roleName)
	{
		UserRole role = new UserRole();
		role.setRole(roleName);
		role.setUser(this);
		getRoles().add(role);
	}
	
	public static User create(String username){
		User user = new User();
		user.setUsername(username);
		user.addRole(UserRole.ROLE_USER);
		return user;
	}
	
}
