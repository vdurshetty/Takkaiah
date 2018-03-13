
package com.takkaiah.db.dto;

import java.util.List;

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
@Table (name="CustomerMaster")
public class Customer {
			
	@GenericGenerator(name="generator", strategy="increment")
	@GeneratedValue(generator="generator")
	@Column(name = "CustID")
	@Id
	private int  custID;
	
	@Column(name = "CustName")
    private String customerName;
	
	
	@ManyToOne
	@JoinColumn(name="CustGroup")
	private CustomerGroup custGroup;
	
	@Column(name = "Address")
	private String address;
	

	@Column(name = "EmailID")
	private String email;
	
	@Column(name = "Mobile")
	private String mobile;

	@Column(name = "Notes")
	private String notes;
	
	
	@Column(name = "remarks")
	private String remarks;
	
	@ManyToOne
	@JoinColumn(name="mrpType")
	private MRPType custMRP; 
	
	
	@OneToMany (mappedBy="mapCustomer")
	private List<CustomerItemMapping> custMapItem;

	
	public Customer() {
		super();
	}



	public int getCustID() {
		return custID;
	}



	public void setCustID(int custID) {
		this.custID = custID;
	}



	public String getCustomerName() {
		return customerName;
	}



	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}



	public CustomerGroup getCustGroup() {
		return custGroup;
	}



	public void setCustGroup(CustomerGroup custGroup) {
		this.custGroup = custGroup;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getMobile() {
		return mobile;
	}



	public void setMobile(String mobile) {
		this.mobile = mobile;
	}



	public String getNotes() {
		return notes;
	}



	public void setNotes(String notes) {
		this.notes = notes;
	}



	public String getRemarks() {
		return remarks;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	public MRPType getCustMRP() {
		return custMRP;
	}



	public void setCustMRP(MRPType custMRP) {
		this.custMRP = custMRP;
	}



	public List<CustomerItemMapping> getCustMapItem() {
		return custMapItem;
	}



	public void setCustMapItem(List<CustomerItemMapping> custMapItem) {
		this.custMapItem = custMapItem;
	}



	


	

	
}