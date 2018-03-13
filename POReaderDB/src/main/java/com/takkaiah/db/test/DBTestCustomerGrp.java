package com.takkaiah.db.test;


import java.util.List;
import java.util.Vector;

import com.takkaiah.db.dao.CustomerGroupDAO;
import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.db.dto.KeyValueMaster;
import com.takkaiah.logger.POReaderLogger;


public class DBTestCustomerGrp {
	static POReaderLogger log = POReaderLogger.getLogger(DBTestCustomerGrp.class.getName());
	public static void main(String a[]) throws Exception{
		
		log.error("In Customer Group Test");
		//addCustomerGrp();
		dispAllCustGrps();
		//getCGNames();
		System.exit(0);
		
	}
	
	


	
	private static void dispAllCustGrps()  throws Exception{
	 CustomerGroupDAO cgDao = new CustomerGroupDAO();
		Long stTime = System.currentTimeMillis();
		List<CustomerGroup> cgroups =  cgDao.getAllCustomerGroups();
		CustomerGroup cg = null;
		int totCount = cgroups.size();
		  for (int i = 0; i < totCount; i++) {  
			  cg = (CustomerGroup) cgroups.get(i);
			   log.error("Category ID :" + cg.getCgID() );
			   log.error(" Customer Grp Name :" + cg.getCgName());
			   log.error(" Customer Grp Alias :" + cg.getCgAlias());
			   log.error(" Address:" + cg.getAddress() );
			   log.error(" Remarks:" + cg.getRemarks() );
			   log.error("_________________");
			  }
		
			Long endTime = System.currentTimeMillis();
			log.error("TIme Taken : " + ( (endTime - stTime)/1000)  + " Secs");
			log.error("Total Records :" + totCount);
	}
	
	
	private static void getCGNames()  throws Exception{
		 CustomerGroupDAO cgDao = new CustomerGroupDAO();
			Vector<KeyValueMaster> cgroups =  cgDao.getCustomerGroupNames();
			KeyValueMaster cg = null;
			int totCount = cgroups.size();
			  for (int i = 0; i < totCount; i++) {  
				  cg = (KeyValueMaster) cgroups.get(i);
				   log.error("Category ID :" + cg.getId() );
				   log.error(" Customer Grp Name :" + cg.getText());
				   log.error("_________________");
				  }
			
		}


	private static void addCustomerGrp() throws Exception {
		CustomerGroup ic = new CustomerGroup();
		ic.setCgName("Customer Group Name 5");
		ic.setCgAlias("CG Alias 1");
		ic.setAddress("My Address 1" );
		ic.setRemarks("my Remarks 1");
		
		CustomerGroupDAO cgDao = new CustomerGroupDAO();
		cgDao.addCustomerGroup(ic);
		
	}

}
