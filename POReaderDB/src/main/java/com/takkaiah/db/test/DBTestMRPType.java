package com.takkaiah.db.test;


import java.util.List;

import com.takkaiah.db.dao.MRPTypeDAO;
import com.takkaiah.db.dto.MRPType;
import com.takkaiah.logger.POReaderLogger;


public class DBTestMRPType {
	
	static POReaderLogger log = POReaderLogger.getLogger(DBTestMRPType.class.getName());
	public static void main(String a[]) throws Exception{
		
		addPriceType();
		dispAllPriceTypes();
	
		System.exit(0);
		
	}
	
	


	
	private static void dispAllPriceTypes()  throws Exception{
		MRPTypeDAO ptDao = new MRPTypeDAO();
		Long stTime = System.currentTimeMillis();
		List<MRPType> itemCats =  ptDao.getAllMRPTypes();
		MRPType mtpType = null;
		int totCount = itemCats.size();
		  for (int i = 0; i < totCount; i++) {  
			  mtpType = (MRPType) itemCats.get(i);
			   log.error("MRP Type ID   :" + mtpType.getMrpTID());
			   log.error("MRP Type Name :" + mtpType.getMrpTName() );
			   log.error("MRP Type Alias:" + mtpType.getAlias());
			   log.error("MRP Remarks   :" + mtpType.getRemarks());
			   log.error("_________________");
			  }
		
			Long endTime = System.currentTimeMillis();
			log.error("TIme Taken : " + ( (endTime - stTime)/1000)  + " Secs");
			log.error("Total Records :" + totCount);
	}
	

	private static void addPriceType() throws Exception {
		MRPType mtpType = new MRPType();
		//ic.setCatID(2);
		mtpType.setMrpTName("MRP Type Name 2 ");
		mtpType.setAlias("mtpType alias");
		mtpType.setRemarks("REmarks Venu");
		MRPTypeDAO ptDao = new MRPTypeDAO();
		ptDao.addMRPType(mtpType);
		
	}

}
