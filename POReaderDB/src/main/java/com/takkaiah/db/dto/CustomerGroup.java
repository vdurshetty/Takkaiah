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
@Table (name="customergroup")
public class CustomerGroup {
			
	@GenericGenerator(name="generator", strategy="increment")
	@GeneratedValue(generator="generator")
	@Column(name = "CustGrpID")
	@Id
	private int  cgID;
	
	@Column(name = "CustGrpName")
    private String cgName;
	
	@Column(name = "CustGrpAlias")
	private String cgAlias;
	
	@Column(name = "Address")
	private String address;
	
	@Column(name = "remarks")
	private String remarks;
	
	@OneToMany (mappedBy="custGroup")
	private List<Customer> customers;
	
	
	@OneToMany (mappedBy="custItemGroup")
	private List<CustomerArticleCode> custArticleCode;

	
	public CustomerGroup() {
		super();
	}

	public int getCgID() {
		return cgID;
	}

	public void setCgID(int cgID) {
		this.cgID = cgID;
	}

	public String getCgName() {
		return cgName;
	}

	public void setCgName(String cgName) {
		this.cgName = cgName;
	}

	public String getCgAlias() {
		return cgAlias;
	}

	public void setCgAlias(String cgAlias) {
		this.cgAlias = cgAlias;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public List<CustomerArticleCode> getCustArticleCode() {
		return custArticleCode;
	}

	public void setCustArticleCode(List<CustomerArticleCode> custArticleCode) {
		this.custArticleCode = custArticleCode;
	}
	
	
	
}