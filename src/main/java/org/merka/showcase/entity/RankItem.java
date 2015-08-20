package org.merka.showcase.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RANKITEM", 
		uniqueConstraints = {
			@UniqueConstraint( columnNames = { "positionInRank" })
		})
public class RankItem
{
	public static final int INVALID_POSITION = -1;
	
	private Long id;
	private String name;
	private String description;
	private int positionInRank;
	
	
	private Rank rank;
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
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
	
	public int getPositionInRank()
	{
		return positionInRank;
	}
	public void setPositionInRank(int positionInRank)
	{
		this.positionInRank = positionInRank;
	}
	
	@ManyToOne(targetEntity = Rank.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
	@JoinColumn(name = "RANK_ID")
	public Rank getRank()
	{
		return rank;
	}
	public void setRank(Rank rank)
	{
		this.rank = rank;
	}
	
	public RankItem()
	{
		setPositionInRank(INVALID_POSITION);
	}
	
	public RankItem(long id, String name, String description, int positionInRank, Rank rank)
	{
		this();
		this.id = id;
		this.name = name;
		this.description = description;
		this.positionInRank = positionInRank;
		this.rank = rank;
	}
	
	public static RankItem create(String name, String description)
	{
		RankItem item = new RankItem();
		item.setName(name);
		item.setDescription(description);
		return item;
	}
}
