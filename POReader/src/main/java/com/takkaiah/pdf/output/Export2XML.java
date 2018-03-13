package com.takkaiah.pdf.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;
import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;

public class Export2XML {
	static POReaderLogger log = POReaderLogger.getLogger(Export2XML.class.getName());

	public static boolean exportPODetails(PurchaseOrderInfo poInfo, List<Object[]> poList,String fileName) { // throws Exception{
		boolean status = false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-YYYY");
		if (fileName==null){
			log.error("Empty file name");
			return false;
		}
		File file = new File(fileName);
		if (file.exists()){
			log.error("File Already Exists");
		} else {
			File folder  =  new File(POReaderReadEnv.getEnvValue(POReaderEnvProp.XMLExportFolder));
			if (!folder.exists() ) {
				folder.mkdirs();
			}
		}
		FileOutputStream fos = null;
		String xmlData = "<POInfo><POHeader>";
		try{
			fos = new FileOutputStream(file);
			xmlData = xmlData + "<PO_Number>" + poInfo.getPoNumber() + "</PO_Number>"; 
			xmlData = xmlData + "<PO_Date>" + sdf.format(poInfo.getPoDate()) + "</PO_Date>"; 
			//xmlData = "<Shipment Date>" + "" + "</Shipment Date>";
			xmlData = xmlData + "<Delivery_Date>" + sdf.format(poInfo.getDeliveryDate())+ "</Delivery_Date>";
			xmlData = xmlData + "<Customer_Name>" + poInfo.getCustomerName() + "</Customer_Name>"; 
			xmlData = xmlData+ "</POHeader>";
			Object cols[] = poList.get(0);

			xmlData = xmlData+ "<POList rowcount=\"" + (poList.size() - 1) + "\">";
			
			for (int i=1;i<poList.size();i++){
				Object row[] = poList.get(i);
				xmlData = xmlData+ "<row num=\"" + i + "\">";
				for (int j=0;j<cols.length;j++){
					String colName = cols[j].toString().replaceAll(" ", "_");
					xmlData = xmlData + "<" + colName + ">" + row[j] + "</" + colName + ">";
				}
				xmlData = xmlData+ "</row>";
			}
			xmlData = xmlData+ "</POList></POInfo>";
			fos.write(xmlData.getBytes());
			fos.close();
			status=true;
		}catch(Exception e){
			log.error("Unable to create XML File :" + e.getMessage());
			//throw new Exception(e.getMessage());
		} finally {
			try {
				fos.close();
			} catch (IOException ex) {
				log.error("Error Closing file stream :" + ex.getMessage());
			}
		}
		return status;
	}

}
