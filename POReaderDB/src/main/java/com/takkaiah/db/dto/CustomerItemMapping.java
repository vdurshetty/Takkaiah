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
@Table (name="CustItemMapping")
public class CustomerItemMapping {
			
	@GenericGenerator(name="generator", strategy="increment")
	@GeneratedValue(generator="generator")
	@Column(name = "CimID")
	@Id
	private int  cimID;
	
		
	@ManyToOne
	@JoinColumn(name="CustID")
	private Customer mapCustomer;
	
	@ManyToOne
	@JoinColumn(name="ItemID")
	private Item custMapItem;
	
	@Column(name="MarginPercent")
	private float marginPercent;
	
		
	public CustomerItemMapping() {
		super();
	}

	public int getCimID() {
		return cimID;
	}

	public void setCimID(int cimID) {
		this.cimID = cimID;
	}

	
	public Customer getMapCustomer() {
		return mapCustomer;
	}

	public void setMapCustomer(Customer mapCustomer) {
		this.mapCustomer = mapCustomer;
	}

	public Item getCustMapItem() {
		return custMapItem;
	}

	public void setCustMapItem(Item custMapItem) {
		this.custMapItem = custMapItem;
	}

	public float getMarginPercent() {
		return marginPercent;
	}

	public void setMarginPercent(float marginPercent) {
		this.marginPercent = marginPercent;
	}

	
	
	
}
