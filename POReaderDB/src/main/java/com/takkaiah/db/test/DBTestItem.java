package com.takkaiah.db.test;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.takkaiah.db.dao.ItemCategoryDAO;
import com.takkaiah.db.dao.ItemDAO;
import com.takkaiah.db.dao.MRPTypeDAO;
import com.takkaiah.db.dao.UserDAO;
import com.takkaiah.db.dto.Customer;
import com.takkaiah.db.dto.Item;
import com.takkaiah.db.dto.ItemCategory;
import com.takkaiah.db.dto.ItemMRP;
import com.takkaiah.db.dto.ItemWithCustomerMaringPercent;
import com.takkaiah.db.dto.MRPType;
import com.takkaiah.db.dto.User;
import com.takkaiah.logger.POReaderLogger;


public class DBTestItem {
	
	static POReaderLogger log = POReaderLogger.getLogger(DBTestItem.class.getName());
	public static void main(String a[]) throws Exception{
		
		log.error("In Customer  Test");
		//addItem();
		//dispAllItems();
		//getUserDetails();
		itemPercentTest();
		System.exit(0);
		
	}
	
	
   private static void getUserDetails() throws Exception{
	   int uid = 2;
	   
	   UserDAO uDAO = new UserDAO();
	   User user =  uDAO.getUser(2);
	   
	   log.error("User id :" + user.getUid() + " - " + user.getUserName());
	   List<Object[]> perms = uDAO.getUserPermissions(user.getUid());
	   for (int i=0;i<perms.size();i++){
		  Object[] perm =   perms.get(i);
			   log.error(perm[0] + " - " + perm[1]);
	//	   log.error(up.getpFunc().getfID() + " - " + up.getpFunc().getfName());
	   }
	   
	   
   }

	
	private static void dispAllItems()  throws Exception{
	ItemDAO cDao = new ItemDAO();
		List<Item> items =  cDao.getAllitems();
		Item item = null;
		int totCount = items.size();
		  for (int i = 0; i < totCount; i++) {  
			  item = (Item) items.get(i);
			   log.error("Item ID :" +  item.getItemID());
			   log.error(" Item Name:" + item.getItemName() );
			   log.error(" Item Alias :" + item.getAlias());
			   log.error(" EAN Code :" + item.getEanCode());
			   log.error(" Item Category :" + item.getItemCategory().getCatName() );
			   log.error(" Units :" + item.getUnits() );
			   
			   List<ItemMRP> itemPrices = item.getItemPrice();
			   if (itemPrices!=null) {
			   for (int j=0;j<itemPrices.size();j++){
				   	ItemMRP ip = itemPrices.get(j);
				   	log.error("MRP ID:" + ip.getiMRPID());
				   	log.error("MRP Type :" + ip.getItemMRPType().getMrpTName());
				   	log.error("MRP :" + ip.getMrp());
			   }
			   }
			   log.error("_________________");
			  }
		
	}
	

	private static void addItem() throws Exception {
		ItemCategoryDAO icDao = new ItemCategoryDAO();
		 
		ItemCategory ic = icDao.getItemCategory(1); 
		
		Item item = new Item();
		item.setItemCategory(ic);
		log.error("Item Category:" + ic.getCatName());
		
		item.setEanCode("EAN100012");
		item.setItemName("Item Name 12");
		item.setAlias("Item Alias 12");
		item.setUnits("Each");
		
		ItemMRP itemPrice1 = new ItemMRP();
		itemPrice1.setItem(item);
		itemPrice1.setItemMRPType(new MRPTypeDAO().getMRPType(1));
		itemPrice1.setMrp((float) 23.89);
		
		ItemMRP itemPrice2 = new ItemMRP();
		itemPrice2.setItem(item);
		itemPrice2.setItemMRPType(new MRPTypeDAO().getMRPType(2));
		itemPrice2.setMrp((float) 44.45);
		
		List<ItemMRP> itemPrices = new ArrayList<>();
		itemPrices.add(itemPrice1);
		itemPrices.add(itemPrice2);
		
		item.setItemPrice(itemPrices);
		
		ItemDAO iDao = new ItemDAO();
		iDao.addItem(item);
	}
	
	
	
	private static void itemPercentTest() throws Exception{
		Customer cust = new Customer();
		MRPType mrpType = new MRPType();
		mrpType.setMrpTID(1);
		cust.setCustID(3); cust.setCustMRP(mrpType);
		String eanCode = "'8906016640792', '8906016640181','8906016641041'";
		
		ItemDAO iDao = new ItemDAO();
		
		Map<String, ItemWithCustomerMaringPercent > iMaps= iDao.getItemDetailsWithCustomerMarginPercent(cust, eanCode);
		
		System.out.println("ItemID | Item EAN | MRP | Percent"  );
		for (String key : iMaps.keySet()) {
			ItemWithCustomerMaringPercent ii = iMaps.get(key);
			System.out.println( ii.getItemId() + " : " + ii.getEanCode() + " : " + ii.getMrp() + " : " + ii.getCustMarginPercent());
		}
		
		
	}

}
