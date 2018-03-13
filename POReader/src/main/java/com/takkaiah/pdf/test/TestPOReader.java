package com.takkaiah.pdf.test;

import java.util.List;

import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.pdf.core.POFormats;
import com.takkaiah.pdf.core.POReader;
import com.takkaiah.pdf.core.POReaderFactory;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;
import com.takkaiah.pdf.vo.PurchaseOrderItems;

public class TestPOReader {
	
	static POReaderLogger log = POReaderLogger.getLogger(TestPOReader.class.getName());
	
	public static void main(String a[]) throws Exception{
		
		
		//POReader poReader = POReaderFactory.getPOReader(POFormats.SpencerPO,true);
		//PurchaseOrderInfo poInfo = poReader.getPODetails("D:/Venu/samples/New/gstpurchaseorders/Spencers Outstation.pdf");
		//PurchaseOrderInfo poInfo = poReader.getPODetails("D:/Venu/samples/New/gstpurchaseorders/Spencers Telangana.pdf");
		
			
		POReader poReader = POReaderFactory.getPOReader(POFormats.ReliancePO,true);
		//PurchaseOrderInfo poInfo = poReader.getPODetails("D:/Venu/samples/New/gstpurchaseorders/Reliance Telangana.pdf");
		PurchaseOrderInfo poInfo = poReader.getPODetails("E:/Venu/Ganesh/gstpurchaseorders/Reliance Outstation.pdf");
		
		
		
		//POReader poReader = POReaderFactory.getPOReader(POFormats.MaxHyperMarketPO,true);
		//PurchaseOrderInfo poInfo = poReader.getPODetails("D:/Venu/samples/New/gstpurchaseorders/Max Hyper Telangana.pdf");
		
		//POReader poReader = POReaderFactory.getPOReader(POFormats.AdityaBirlaPO,true);
		//PurchaseOrderInfo poInfo = poReader.getPODetails("D:/Venu/samples/New/gstpurchaseorders/Aditya Birla Telangana.pdf");

		//POReader poReader = POReaderFactory.getPOReader(POFormats.HiperCityPO ,true);
		//PurchaseOrderInfo poInfo = poReader.getPODetails("D:/Venu/samples/New/gstpurchaseorders/Hypercity Telangana.pdf");

		//POReader poReader = POReaderFactory.getPOReader(POFormats.BigBazarPO,true);
		//PurchaseOrderInfo poInfo = poReader.getPODetails("D:/Venu/samples/New/Big Bazar PO.pdf");

		printPOInfo(poInfo);
		
		//String poFileList[] = new String[4];
		
		/*
		poFileList[0] = "D:/Venu/samples/Reliance/RELIANCE.pdf";
		poFileList[1] = "D:/Venu/samples/Reliance/RELIANCE1.pdf";
		poFileList[2] = "D:/Venu/samples/Reliance/RELIANCE2.pdf";
		poFileList[3] = "D:/Venu/samples/Reliance/RELIANCE3.pdf";
		
		POReaderList poReaderList = POReaderListFactory.getPOReader(POFormats.reliancePO);
		*/

		/*
		poFileList[0] = "D:/Venu/samples/Spencer/Spencer.pdf";
		poFileList[1] = "D:/Venu/samples/Spencer/Spencer1.pdf";
		poFileList[2] = "D:/Venu/samples/Spencer/Spencer2.pdf";
		poFileList[3] = "D:/Venu/samples/Spencer/Spencer3.pdf";
	
		POReaderList poReaderList = POReaderListFactory.getPOReader(POFormats.spencerPO);
		*/

		
		/*	
		poFileList[0] = "D:/Venu/samples/ab/Aditya Birla.pdf";
		poFileList[1] = "D:/Venu/samples/ab/Aditya Birla1.pdf";
		poFileList[2] = "D:/Venu/samples/ab/Aditya Birla2.pdf";
		poFileList[3] = "D:/Venu/samples/ab/Aditya Birla3.pdf";
	
		POReaderList poReaderList = POReaderListFactory.getPOReader(POFormats.adityaBirlaPO);
		*/

		/*	
		poFileList[0] = "D:/Venu/samples/HyperCITY/HyperCITY.pdf";
		poFileList[1] = "D:/Venu/samples/HyperCITY/HyperCITY1.pdf";
		poFileList[2] = "D:/Venu/samples/HyperCITY/HyperCITY2.pdf";
		poFileList[3] = "D:/Venu/samples/HyperCITY/HyperCITY3.pdf";
	
		POReaderList poReaderList = POReaderListFactory.getPOReader(POFormats.hiperCityPO);
		*/

		/*	
		poFileList[0] = "D:/Venu/samples/max/Max Hypermarket.pdf";
		poFileList[1] = "D:/Venu/samples/max/Max Hypermarket1.pdf";
		poFileList[2] = "D:/Venu/samples/max/Max Hypermarket2.pdf";
		poFileList[3] = "D:/Venu/samples/max/Max Hypermarket3.pdf";
	     
		POReaderList poReaderList = POReaderListFactory.getPOReader(POFormats.maxHyperMarketPO);
		*/

		
		/*
		 Hashtable<String, POReadStatus> poList = poReaderList.getPODetails(poFileList);
		
		Set<String> keys = poList.keySet();
		
		Iterator<String> itr = keys.iterator();
		
		while(itr.hasNext()){
			String key = itr.next();
			log.error(key);
		    POReadStatus poStatus = (POReadStatus) poList.get(key);
		    if (poStatus.isSuccess()){
		    	
		    	printPOInfo(poStatus.getPoInfo());
		    } else {
		    	log.error("Error " + poStatus.getErrorMsg());
		    }
		} 
		*/
		
		System.exit(0);
	}
	
	
    private static void printPOInfo(PurchaseOrderInfo poInfo){
    	log.error("poNumber:" + poInfo.getPoNumber());
    	log.error("poDate:" + poInfo.getPoDate());
    	log.error("Customer Name:" + poInfo.getCustomerName() );
    	log.error("Delivery Date:" + poInfo.getDeliveryDate());
    	log.error("PO Expiry Date:" + poInfo.getPoExpiryDate());
    	log.error("Outstation PO:" + poInfo.isOutStation());
    	float totalValue = 0;
    	log.error("\nItem No | HSN No   | Article No | EAN No     | Material Desc   |Quantity | UOM   |MRP   | Basic Cost   | GST  | SGST | SalesTax   |Total Basic Value ");
    	List<PurchaseOrderItems> poItems = poInfo.getPoItems();	
    	if (poItems !=null) {
		for (int i=0;i<poItems.size();i++ ){
			PurchaseOrderItems poItem = poItems.get(i);
			log.error( poItem.getItemNo() + " | " + poItem.getHsnNo() + " | " + poItem.getArticleNo() + " | " +
			poItem.getEanNo() + " | " + poItem.getMaterialDesc() + " | " + poItem.getQuantity() + " | " +
			poItem.getUom() + " | " + poItem.getMrp() + " | " + poItem.getBasicCost() + " | " + poItem.getCgst() + " | " + poItem.getSgst() + "|" + poItem.getSalesTax() + " | " + poItem.getTotalBaseValue());
			totalValue = totalValue + poItem.getTotalBaseValue();
		}
		System.out.println("Total Price :"+ totalValue);
    	}
    }
  
}
