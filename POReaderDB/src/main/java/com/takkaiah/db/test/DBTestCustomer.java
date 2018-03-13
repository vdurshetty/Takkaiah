package com.takkaiah.db.test;


import java.util.List;

import com.takkaiah.db.dao.CustomerDAO;
import com.takkaiah.db.dao.CustomerGroupDAO;
import com.takkaiah.db.dto.Customer;
import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.db.dto.MRPType;
import com.takkaiah.logger.POReaderLogger;


public class DBTestCustomer {
	
	static POReaderLogger log = POReaderLogger.getLogger(DBTestCustomer.class.getName());
	public static void main(String a[]) throws Exception{
		
		log.error("In Customer  Test");
		addCustomer();
		//dispAllcustomers();
		//updateCustomer();
		System.exit(0);
		
	}
	
	


	
	private static void dispAllcustomers()  throws Exception{
	 CustomerDAO cDao = new CustomerDAO();
		List<Customer> customers =  cDao.getAllCustomers();
		Customer cust = null;
		int totCount = customers.size();
		  for (int i = 0; i < totCount; i++) {  
			  cust = (Customer) customers.get(i);
			   log.error("Customer ID :" + cust.getCustID() );
			   log.error(" Customer Name :" + cust.getCustomerName());
			   CustomerGroup cg = cust.getCustGroup();
			   if (cg!=null){
			   log.error(" Customer Grp Name :" + cust.getCustGroup().getCgName());
			   }
			   log.error(" Address:" + cust.getAddress() );
			   log.error(" MRP Type Name :" + cust.getCustMRP().getMrpTName() );
			   log.error(" Email :" + cust.getEmail() );
			   log.error(" Mobile :" + cust.getMobile() );
			   log.error(" Notes :" + cust.getNotes() );
			   log.error(" Remarks:" + cust.getRemarks() );
			   log.error("_________________");
			  }
		
	}
	

	private static void addCustomer() throws Exception {
		CustomerGroupDAO cgDao = new CustomerGroupDAO();
		 
		CustomerGroup cg = cgDao.getCustomerGroup(1); 
		Customer cust = new Customer();
		cust.setCustGroup(cg);
		log.error("Customer Group :" + cg.getCgName());
		
		cust.setCustomerName("Customer Name 3");
		cust.setEmail("My Email");
		cust.setMobile("3343434");
		cust.setNotes("Notes");
		cust.setAddress("My Address1");
		MRPType mrpType = new MRPType();
		mrpType.setMrpTID(1);
		cust.setCustMRP(mrpType);
		cust.setRemarks("my Remarks");
		
		CustomerDAO cDao = new CustomerDAO();
		cDao.addCustomer(cust);
	}
	
	private static void updateCustomer() throws Exception {
		CustomerGroupDAO cgDao = new CustomerGroupDAO();
		 
		CustomerGroup cg = cgDao.getCustomerGroup(1); 
		Customer cust = new Customer();
		cust.setCustGroup(cg);
		log.error("Customer Group :" + cg.getCgName());
		
		cust.setCustID(311);
		cust.setCustomerName("Customer Name 3");
		cust.setEmail("My Email");
		cust.setMobile("3343434");
		cust.setNotes("Notes");
		cust.setAddress("My Address1");
		MRPType mrpType = new MRPType();
		mrpType.setMrpTID(1);
		cust.setCustMRP(mrpType);
		cust.setRemarks("my Remarks");
		
		CustomerDAO cDao = new CustomerDAO();
		cDao.updateCustomer(cust);
	}


}
