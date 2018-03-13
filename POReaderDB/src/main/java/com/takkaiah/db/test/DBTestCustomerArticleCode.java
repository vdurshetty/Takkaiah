package com.takkaiah.db.test;


import java.util.ArrayList;
import java.util.List;

import com.takkaiah.db.dao.CustomerArticleCodeDAO;
import com.takkaiah.db.dao.CustomerGroupDAO;
import com.takkaiah.db.dao.ItemDAO;
import com.takkaiah.db.dto.CustomerArticleCode;
import com.takkaiah.db.dto.CustomerGroup;
import com.takkaiah.db.dto.Item;
import com.takkaiah.logger.POReaderLogger;


public class DBTestCustomerArticleCode {
	
	static POReaderLogger log = POReaderLogger.getLogger(DBTestCustomerArticleCode.class.getName());
	public static void main(String a[]) throws Exception{
		
		log.error("In Customer  Test");
		//addCustArticleCode();
		dispAllArticleCodes();
	
		System.exit(0);
		
	}
	
	
	
	
	private static void dispAllArticleCodes()  throws Exception{
		CustomerArticleCodeDAO caDao = new CustomerArticleCodeDAO();
		List<Object[]> custrticleCodes = caDao.getAllCustArticleCodes(new CustomerGroupDAO().getCustomerGroup(1));
		
		Object[] custACode = null;
	    for (int i = 0; i < custrticleCodes.size(); i++) {  
	    	 custACode = custrticleCodes.get(i);
			   log.error("Customer Article Code ID :" + custACode[0] );
			   log.error(" Item ID :" + custACode[1]);
			   log.error(" Item Name :" + custACode[2]);
			   log.error(" Item EAN  :" + custACode[3]);
			   log.error(" Customer Article Code  :" + custACode[4]);
			   log.error("_________________");
			  }
		
	}
	

	private static void addCustArticleCode() throws Exception {
		CustomerGroupDAO cgDao = new CustomerGroupDAO();
		CustomerGroup cg = cgDao.getCustomerGroup(1);
		
		ItemDAO iDao = new ItemDAO();
		Item item = null;
		
		List<CustomerArticleCode> custrticleCodes = new ArrayList<>();
		
		CustomerArticleCode custACode = new CustomerArticleCode();
		custACode.setArticleCode("192001");
		custACode.setCustItemGroup(cg);
		custACode.setCustItem(iDao.getItem(1));
		
		custrticleCodes.add(custACode);

		custACode = new CustomerArticleCode();
		custACode.setArticleCode("192002");
		custACode.setCustItemGroup(cg);
		custACode.setCustItem(iDao.getItem(2));

		custrticleCodes.add(custACode);

		custACode = new CustomerArticleCode();
		custACode.setArticleCode("192003");
		custACode.setCustItemGroup(cg);
		custACode.setCustItem(iDao.getItem(3));

		custrticleCodes.add(custACode);
		
		CustomerArticleCodeDAO caDao = new CustomerArticleCodeDAO();
		
		caDao.addUpdateCustomerArticleCode(custrticleCodes);
	}

}
