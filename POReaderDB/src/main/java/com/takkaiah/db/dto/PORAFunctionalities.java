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
@Table (name="PORA_Functionalities")
public class PORAFunctionalities {
	
		@Id
		@GenericGenerator(name="generator", strategy="increment")
	    @GeneratedValue(generator="generator")
		private int fID;
		private String fName;
		@Column(name = "description")
		private String desc;
		
		@OneToMany (mappedBy="pFunc")
		private List<UserPermissions> permissions;

		public PORAFunctionalities() {
			super();
		}
		
		public int getfID() {
			return fID;
		}
		public void setfID(int fID) {
			this.fID = fID;
		}
		public String getfName() {
			return fName;
		}
		public void setfName(String fName) {
			this.fName = fName;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}

	
		

		
}
