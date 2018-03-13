package com.takkaiah.db.dto;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table (name="ItemCategoryMaster")
public class ItemCategory {
	
	@Id
	@GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
	private int catID;
	private String catName;
	private String catAlias ;
	
	@OneToMany (mappedBy="itemCategory")
	private List<Item> items;

	
	
	public ItemCategory() {
		super();
	}
	/**
	 * @param catID
	 * @param catName
	 * @param taxPercent
	 */
	public ItemCategory(int catID, String catName, String catAlias) {
		super();
		this.catID = catID;
		this.catName = catName;
		this.catAlias = catAlias;
	}
	
	public int getCatID() {
		return catID;
	}
	public void setCatID(int catID) {
		this.catID = catID;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getCatAlias() {
		return catAlias;
	}
	public void setCatAlias(String catAlias) {
		this.catAlias = catAlias;
	}
	
	
}
