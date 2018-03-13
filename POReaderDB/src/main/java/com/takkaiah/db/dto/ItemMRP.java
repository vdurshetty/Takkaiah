package com.takkaiah.db.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table (name="ItemMRP")
public class ItemMRP {
			
	@GenericGenerator(name="generator", strategy="increment")
	@GeneratedValue(generator="generator")
	@Column(name = "imprID")
	@Id
	private int  iMRPID;
	
	@Column(name = "mrp")
	private float mrp;
	
	@Transient
	//@Column(insertable=false, updatable=false, nullable=true)
	private String rowStatus;
	
	
	//@ManyToOne(cascade=CascadeType.ALL, optional=true, fetch=FetchType.EAGER)  
	@ManyToOne(optional=true, fetch = FetchType.LAZY)  
	@JoinColumn(name="ItemID")  
	private Item item;
	

	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="mrpType", nullable=false)
	private MRPType itemMRPType;

		
	public ItemMRP() {
		super();
	}


	public int getiMRPID() {
		return iMRPID;
	}


	public void setiMRPID(int iMRPID) {
		this.iMRPID = iMRPID;
	}


	public float getMrp() {
		return mrp;
	}


	public void setMrp(float mrp) {
		this.mrp = mrp;
	}


	public Item getItem() {
		return item;
	}


	public void setItem(Item item) {
		this.item = item;
	}


	public MRPType getItemMRPType() {
		return itemMRPType;
	}


	public void setItemMRPType(MRPType itemMRPType) {
		this.itemMRPType = itemMRPType;
	}


	public String getRowStatus() {
		return rowStatus;
	}


	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}

	

}
