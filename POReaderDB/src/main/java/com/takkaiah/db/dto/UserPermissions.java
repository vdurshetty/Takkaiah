package com.takkaiah.db.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table (name="userPermissions")
public class UserPermissions implements Serializable{
	
	private static final long serialVersionUID = 1L;

	//@Id 
	//@Column(insertable=false)
	//private int id;
	
	@Id
	@ManyToOne(optional=true, fetch = FetchType.LAZY)  
	@JoinColumn(name="uid")  
	private User pUser;
	
	
	@ManyToOne
	@JoinColumn(name="fid")
	private PORAFunctionalities pFunc;

	public User getpUser() {
		return pUser;
	}

	public void setpUser(User pUser) {
		this.pUser = pUser;
	}

	public PORAFunctionalities getpFunc() {
		return pFunc;
	}

	public void setpFunc(PORAFunctionalities pFunc) {
		this.pFunc = pFunc;
	}

	
	
}
