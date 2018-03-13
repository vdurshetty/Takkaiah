package com.takkaiah.db.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

	
@Entity
@Table (name="CGArticleCodes")
public class CustomerArticleCode {
			
	@GenericGenerator(name="generator", strategy="increment")
	@GeneratedValue(generator="generator")
	@Column(name = "cgAID")
	@Id
	private int  cgAID;
	
	@Column(name = "ArticleCode")
    private String articleCode;
	
		
	@ManyToOne
	@JoinColumn(name="cgID")
	private CustomerGroup custItemGroup;
	
	@ManyToOne 
	@JoinColumn(name="ItemID")
	private Item custItem;
		
	public CustomerArticleCode() {
		super();
	}

	public int getCgAID() {
		return cgAID;
	}

	public void setCgAID(int cgAID) {
		this.cgAID = cgAID;
	}

	public String getArticleCode() {
		return articleCode;
	}

	public void setArticleCode(String articleCode) {
		this.articleCode = articleCode;
	}

	public CustomerGroup getCustItemGroup() {
		return custItemGroup;
	}

	public void setCustItemGroup(CustomerGroup custItemGroup) {
		this.custItemGroup = custItemGroup;
	}

	public Item getCustItem() {
		return custItem;
	}

	public void setCustItem(Item custItem) {
		this.custItem = custItem;
	}

	
	
	
}
