
package com.takkaiah.pdf.read;


import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.pdf.exception.POFormatException;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;
import com.takkaiah.pdf.vo.PurchaseOrderItems;

public class BigBazarPDF  {
	
	POReaderLogger log = POReaderLogger.getLogger(BigBazarPDF.class.getName());
	 PurchaseOrderInfo poInfo;
	 int docPages = 0;
	 
	 public BigBazarPDF(){

	 }

	public PurchaseOrderInfo getPODetails(String poFile) throws POFormatException {
		PDDocument document = null;
		 try
         {
			 poInfo = new PurchaseOrderInfo();
			 document  = PDDocument.load( new File(poFile) );
	         docPages = document.getNumberOfPages();
	         fetchPOHeader(document);
	         fetchPORows(document);
         }catch(Exception e){
        	 log.error("Error getting PO details:"+e.getMessage());
			throw new POFormatException(e.getMessage());
         } finally{
        	  if( document != null )
	          {
	                  try {
						document.close();
					} catch (IOException e) {
						log.error("Error closing pdf document :"+e.getMessage());
					}
	         }
         }
		 return poInfo;
	}
	
	

	 
	 private void fetchPOHeader(PDDocument document) throws Exception
	 {
	      try
          {
	    	  poInfo = new PurchaseOrderInfo();
			  PDFTextStripperByArea stripper = new PDFTextStripperByArea();
	          stripper.setSortByPosition( true );
	          
	    	  PDPage firstPage = document.getPage(0);
	    	  
              Rectangle rect1 = new Rectangle( 0, 100, 400, 10 );
              Rectangle rect2 = new Rectangle( 270,160, 300, 100 );
              //Rectangle rect3 = new Rectangle( 230,80, 200, 150 );
      
              stripper.addRegion( "customerName", rect1 );
              stripper.addRegion( "poInfo", rect2 );
                   
              stripper.extractRegions( firstPage );
                
              poInfo.setCustomerName(stripper.getTextForRegion( "customerName" ).trim());
              fetchPOInfo( stripper.getTextForRegion( "poInfo" ).trim());
  
              } catch(Exception e){
            	  log.error("Error getting PO Header details:"+e.getMessage());
            	  throw new Exception(e.getMessage());
              }
  
	 }
	 
	
	    private  void fetchPOInfo(String poData) throws Exception{
	    	 String lines[];
	    	 try{
	    		 lines = poData.split("\n");
	    		 if(lines.length>0){
	         		 if (lines[0].contains("P.O. Number")==true)
	         		 {
	        			 String postr[] = lines[0].split(":");
	        			 if (postr.length>0)
	        				 poInfo.setPoNumber(postr[1].trim().replaceAll("-", ""));
	        		 }
	         		 if (lines[1].contains("Date")==true)
	         		 {
	         			 String podt[] = lines[1].split(":");
	         			 if (podt.length>0) {
	         				 DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	         				 poInfo.setPoDate(dateFormat.parse(podt[1].trim()));
	         				 Calendar cal = Calendar.getInstance();
	         				 cal.setTime(poInfo.getPoDate());
	         				 cal.add(Calendar.DATE, 15);
	             			 poInfo.setDeliveryDate(cal.getTime());
		       			 }
	         		 } 
	         		
	         		if (lines[6].contains("PO expiry date")==true){
	         			String poExDt[] = lines[6].split(":");
	         			 if (poExDt.length>0) {
	         				 DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	         				 poInfo.setPoExpiryDate(dateFormat.parse(poExDt[1].trim()));
		       			 }   
	         		}
	
	    		 }
	    	 }catch(Exception e){
	        	 log.error("Error getting PO Header details:"+e.getMessage());
	    		 throw new Exception(e.getMessage());
	    	 }
        }

	    
	    
	    
	    
	    private void fetchPORows(PDDocument document) throws Exception{
	    	
	    	   try {
			 		int poItemsStartPage = 1;
			 		String poLines = ""; 
			 		boolean startPage = false;
			 		boolean endPage = false;
			 		for  (int i= poItemsStartPage;i <= docPages;i++ ) {
			        	 PDFTextStripper stripper = new PDFTextStripper();
			        	 stripper.setSortByPosition( true );
			        	 stripper.setStartPage( i );
			        	 stripper.setEndPage(i);
			        	 String pdfText = stripper.getText(document);
			        	 if (pdfText.indexOf("Price")>0) {
			        		 pdfText = pdfText.substring(pdfText.indexOf("Price") + 7);
			                 startPage = true;
			        	 } 
			        	 
			        	 if(pdfText.indexOf("Net Amount")>0)   
			        	 {
			        		 pdfText = pdfText.substring(0, pdfText.indexOf("Net Amount"));
			        		 endPage=true;
			        	 }

			        	 if (startPage==true && endPage==true){
			        		 poLines = poLines + pdfText;
			        	 }
			        	 startPage=false;
			        	 endPage=false;
			 		}
			 		poLines = PDFReaderUtil.removeEmptyLines(poLines);
		           	List<PurchaseOrderItems> poItems = fetchPODeails(poLines);
		           	poInfo.setPoItems(poItems);  
		        } catch (Exception e) {
		        	log.error("Error getting PO Item details:"+e.getMessage());
		            throw new Exception(e.getMessage());
		        } 
	    }
	    
	

	private  List<PurchaseOrderItems> fetchPODeails(String poData) throws Exception{
		 List<PurchaseOrderItems> poDetails = new ArrayList<>();
		 String lines[];
		 int rowNumber=1;
		 try{
			 lines = poData.trim().split("\n"); // new line
			 for (int i=0;i<lines.length;i++){
				 String rows[] =  PDFReaderUtil.removeExtraSpaces(lines[i]).split(" ");
				 if (rows.length>8)
				 { 
					 PurchaseOrderItems poItem = new PurchaseOrderItems();
					 poItem.setItemNo(rowNumber++);
					 poItem.setEanNo(rows[1]);
					 poItem.setArticleNo(rows[2]);
					 poItem.setMrp(Float.parseFloat(rows[rows.length-7]));
					 poItem.setBasicCost(Float.parseFloat(rows[rows.length-6]));
					 poItem.setSalesTax(Float.parseFloat(rows[rows.length-5]));
					 poItem.setQuantity(Float.parseFloat(rows[rows.length-3]));
					 poItem.setUom(rows[rows.length-2]);
					 poItem.setTotalBaseValue(Float.parseFloat(rows[rows.length-1].replaceAll(",", "")));
					 if (rows.length==12){
						 poItem.setMaterialDesc(rows[3] + " " + rows[4]);
					 } else if (rows.length==11){
						 poItem.setMaterialDesc(rows[3]);
					 }
	
					 if (i<lines.length-1){
						 String descRows[] =  PDFReaderUtil.removeExtraSpaces(lines[i+1]).split(" ");
						 if (descRows.length<5){
							 poItem.setMaterialDesc(poItem.getMaterialDesc() + " " + lines[i+1].trim()); 
						 }
						 if (i<lines.length-2){
							String subDescRows[] =  PDFReaderUtil.removeExtraSpaces(lines[i+2]).split(" ");
							if (subDescRows.length<5){
								 poItem.setMaterialDesc( poItem.getMaterialDesc() + " " + lines[i+2]); 
							 }
						 }
					 } 
					 poDetails.add(poItem);
				 } 
			 }
		 }catch(Exception e){
			 log.error("Error getting PO Item details into PO Object:"+e.getMessage());
			 throw new Exception(e.getMessage());
		 }
		 return poDetails;
	}
		
	
	
}
