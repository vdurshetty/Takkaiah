package com.takkaiah.db.test;


import java.util.List;

import com.takkaiah.db.dao.ItemCategoryDAO;
import com.takkaiah.db.dto.ItemCategory;
import com.takkaiah.logger.POReaderLogger;


public class DBTestItemCategory {
	static POReaderLogger log = POReaderLogger.getLogger(DBTestItemCategory.class.getName());
	public static void main(String a[]) throws Exception{
		
		addItemCategory();
		dispAllItemCats();
	
		System.exit(0);
		
	}
	
	
	private static void showICDetails(int catid) throws Exception{
	  ItemCategory ic = new ItemCategoryDAO().getItemCategory(catid);
	  log.error("_______Employee Details__________");
	  log.error("Item Category ID :" + ic.getCatID());
	  log.error("Category Name :" + ic.getCatName());
	  log.error("Alias :" + ic.getCatAlias());
	  log.error("_________________");
	}


	
	private static void dispAllItemCats()  throws Exception{
	 ItemCategoryDAO icDao = new ItemCategoryDAO();
		Long stTime = System.currentTimeMillis();
		List<ItemCategory> itemCats =  icDao.getAllItemCategory();
		ItemCategory ic = null;
		int totCount = itemCats.size();
		  for (int i = 0; i < totCount; i++) {  
			  ic = (ItemCategory) itemCats.get(i);
			   log.error("Category ID :" + ic.getCatID());
			   log.error(" Name :" + ic.getCatName());
			   log.error(" Alias :" + ic.getCatAlias());
			   log.error("_________________");
			  }
		
			Long endTime = System.currentTimeMillis();
			log.error("TIme Taken : " + ( (endTime - stTime)/1000)  + " Secs");
			log.error("Total Records :" + totCount);
	}
	

	private static void addItemCategory() throws Exception {
		ItemCategory ic = new ItemCategory();
		//ic.setCatID(2);
		ic.setCatName("Computer Block 22");
		ic.setCatAlias("Alias");
		ItemCategoryDAO icDao = new ItemCategoryDAO();
		icDao.addItemCategory(ic);
		
	}

}
