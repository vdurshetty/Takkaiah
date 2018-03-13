package com.takkaiah.db.dto;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table (name="MRPType")
public class MRPType {
	
	@Id
	@GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
	private int mrpTID;
	private String mrpTName;
	@Column(name = "mrpTAlias")
	private String Alias;
	private String remarks;
	private float mrpTaxPercent;
	
	
	@OneToMany (mappedBy="custMRP")
	private List<Customer> customerMRP;
	
	
	
	@OneToMany (mappedBy="itemMRPType")
	private List<ItemMRP> itemMRP; 
		
	public MRPType() {
		super();
	}



	public int getMrpTID() {
		return mrpTID;
	}



	public void setMrpTID(int mrpTID) {
		this.mrpTID = mrpTID;
	}



	public String getMrpTName() {
		return mrpTName;
	}



	public void setMrpTName(String mrpTName) {
		this.mrpTName = mrpTName;
	}



	public String getAlias() {
		return Alias;
	}



	public void setAlias(String alias) {
		Alias = alias;
	}



	public String getRemarks() {
		return remarks;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	public float getMrpTaxPercent() {
		return mrpTaxPercent;
	}



	public void setMrpTaxPercent(float mrpTaxPercent) {
		this.mrpTaxPercent = mrpTaxPercent;
	}



	public List<Customer> getCustomerMRP() {
		return customerMRP;
	}



	public void setCustomerMRP(List<Customer> customerMRP) {
		this.customerMRP = customerMRP;
	}



	public List<ItemMRP> getItemMRP() {
		return itemMRP;
	}



	public void setItemMRP(List<ItemMRP> itemMRP) {
		this.itemMRP = itemMRP;
	}


	
}
