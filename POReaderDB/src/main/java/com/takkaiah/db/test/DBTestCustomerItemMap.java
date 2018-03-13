package com.takkaiah.db.test;


import java.util.ArrayList;
import java.util.List;

import com.takkaiah.db.dao.CustomerDAO;
import com.takkaiah.db.dao.CustomerItemMappingDAO;
import com.takkaiah.db.dao.ItemDAO;
import com.takkaiah.db.dto.Customer;
import com.takkaiah.db.dto.CustomerArticleCode;
import com.takkaiah.db.dto.CustomerItemMapping;
import com.takkaiah.db.dto.Item;
import com.takkaiah.logger.POReaderLogger;


public class DBTestCustomerItemMap {
	static POReaderLogger log = POReaderLogger.getLogger(DBTestCustomerItemMap.class.getName());
	public static void main(String a[]) throws Exception{
		
		log.error("In Customer  Test");
		//addCustItemMap();
		dispAllCustItemMaps();
		getCIM();
		System.exit(0);
		
	}
	
	
	private static void getCIM() throws Exception{
		
		CustomerItemMapping cim = null;
		
		CustomerItemMappingDAO caDao = new CustomerItemMappingDAO();
		cim = caDao.getCustomerItemMapping(2, 21);
		System.out.println("tax margin :" + cim.getMarginPercent() );
		
	}
	
	
	
	private static void dispAllCustItemMaps()  throws Exception{
		CustomerItemMapping cim = new CustomerItemMapping();
		Customer cust = new Customer(); cust.setCustID(1);
		cim.setMapCustomer(cust);
		
		CustomerItemMappingDAO caDao = new CustomerItemMappingDAO();
		List<Object[]> custItemMaps = caDao.getAllCustItemMappings(cust);
		
		CustomerArticleCode custACode = null;
	    for (int i = 0; i < custItemMaps.size(); i++) {  
	    	   Object [] row = (Object[]) custItemMaps.get(i);
			   log.error("Customer Item Mapping  Code :" + row[0] );
			   log.error(" Item ID :" + row[1] );
			   log.error(" Item Name  :" + row[2]);
			   log.error(" Item EAN  :" + row[3]);
			   log.error(" Item Article Code  :" + row[4]);
			   log.error(" Item Margin percent :" + row[5]);
			   log.error("_________________");
			  }
		
	}
	

	private static void addCustItemMap() throws Exception {
		CustomerDAO cgDao = new CustomerDAO();
		Customer cust = cgDao.getCustomer(1);
		
		ItemDAO iDao = new ItemDAO();
		Item item = null;
		
		List<CustomerItemMapping> customerItemMapps = new ArrayList<>();
		
		CustomerItemMapping custICode = new CustomerItemMapping();
		//custICode.setMarginPercent((float)21.11);
		custICode.setMapCustomer(cust);
		custICode.setCustMapItem(iDao.getItem(1));
		
		
		customerItemMapps.add(custICode);

		custICode = new CustomerItemMapping();
		//custICode.setMarginPercent((float)22.22);
		custICode.setMapCustomer(cust);
		custICode.setCustMapItem(iDao.getItem(2));
		custICode.setMarginPercent(Float.parseFloat("22.22"));
	
		customerItemMapps.add(custICode);

		custICode = new CustomerItemMapping();
		//custICode.setMarginPercent((float)22.33);
		custICode.setMapCustomer(cust);
		custICode.setCustMapItem(iDao.getItem(3));
		custICode.setMarginPercent(Float.parseFloat("11.22"));

		customerItemMapps.add(custICode);
	
		CustomerItemMappingDAO caDao = new CustomerItemMappingDAO();
		
		caDao.addUpdateCustomerItemMappings(customerItemMapps);
	}

}
