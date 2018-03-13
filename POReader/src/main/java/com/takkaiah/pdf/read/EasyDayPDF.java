
package com.takkaiah.pdf.read;


import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.pdf.exception.POFormatException;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;
import com.takkaiah.pdf.vo.PurchaseOrderItems;

public class EasyDayPDF  {
	
	 POReaderLogger log = POReaderLogger.getLogger(EasyDayPDF.class.getName());
	 PurchaseOrderInfo poInfo;
	 int docPages = 0;
	 final String columnDelim = PDFReaderUtil.POColumnsDelimeter;
	 public EasyDayPDF(){

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
						log.error("Error closing po file:"+e.getMessage());
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
	    	  
              Rectangle rect1 = new Rectangle( 10, 120, 400, 20 );
              Rectangle rect2 = new Rectangle( 300,190, 170, 70 );
              //Rectangle rect3 = new Rectangle( 230,80, 200, 150 );
      
              stripper.addRegion( "customerName", rect1 );
              stripper.addRegion( "poInfo", rect2 );
                   
              stripper.extractRegions( firstPage );
              
              //System.out.println(stripper.getTextForRegion( "poInfo" ).trim());
              
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
	         		 if (lines[0].toUpperCase().contains("P.O. NUMBER")==true)
	         		 {
	        			 String postr[] = lines[0].split(":");
	        			 if (postr.length>0)
	        				 poInfo.setPoNumber(postr[1].trim());
	        		 }
	         		 if (lines[1].toUpperCase().contains("DATE")==true)
	         		 {
	         			 String podt[] = lines[1].split(":");
	         			 if (podt.length>0) {
	         				 DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	         				 poInfo.setPoDate(dateFormat.parse(podt[1].trim()));
	         			 }
	         		 }
	         		 if (lines[2].toUpperCase().contains("DELIVERY DATE")==true){
	         			 String podt[] = lines[2].split(":");
	         			 if (podt.length>0) {
	         				 DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	         				 poInfo.setDeliveryDate(dateFormat.parse(podt[1].trim()));
	         			 }
	         		 }
	         		 
	         		 if (lines[3].toUpperCase().contains("PO EXPIRY DATE")==true){
	         			 String poExpDate[] = lines[3].split(":");
	         			 if (poExpDate.length>0) {
	         				 DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	         				 poInfo.setPoExpiryDate(dateFormat.parse(poExpDate[1].trim()));
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
			        	 //System.out.println(pdfText);
			        	 
			        	 if (pdfText.indexOf("Goods")>0) {
			        		 pdfText = pdfText.substring(pdfText.indexOf("Goods") + 5);
			        	 //if (pdfText.indexOf("Amount Value")>0) {
				        	//	 pdfText = pdfText.substring(pdfText.indexOf("Amount Value") + 14);
			                 startPage = true;
			        	 } 
			        	 
			        	 if (pdfText.indexOf("Total")>0){
			        		 pdfText = pdfText.substring(0, pdfText.indexOf("Total"));
			        		 endPage=true;
			        		 //i= docPages+2;  // End looping the pages if Total reached.
			        	 } 

			        	 if (startPage==true && endPage==true){
			        		 poLines = poLines + pdfText.trim();
			        	 }
			        	 startPage=false;
			        	 endPage=false;
			 		}
			 		
			 		System.out.println(poLines + " \n\n");
			 		poLines = PDFReaderUtil.removeEmptyLines(poLines);
			 		
			 		poLines = alignRows(poLines);
		           	List<PurchaseOrderItems> poItems = fetchPODeails(poLines);
		           	poInfo.setPoItems(poItems);
		        } catch (Exception e) {
		        	log.error("Error getting PO Item details:"+e.getMessage());
		            throw new Exception(e.getMessage());
		        } 
	    }
	    
		// This method will align the scrambled row     
		private String alignRows(String poData) throws Exception{
			String retPoData = "";
			String lines[];
			String itemName ="";
			String currLine ="";
			String nextLine ="";
			String currLineCols[];
			String nextLineCols[];
			String cols[];
			int inc=0;
			try{
				lines = poData.trim().split("\n"); 
				for (int i=0;i<lines.length;i++)
				{
						currLine = lines[i].replace("%", "").trim();
						currLine = PDFReaderUtil.removeExtraSpaces(currLine);
						nextLine = PDFReaderUtil.removeExtraSpaces(lines[i+1]);
						inc =0;
						itemName = lines[i+2] +  " " + lines[i+3];
						if ( (i + 4) < lines.length ) 
						{
							cols = lines[i + 4].split(" ");
							if ( cols.length < 10) 
							{
								inc = 1;
								itemName = itemName + " " + lines[i + 4];
								if ( (i + 5) < lines.length ) 
								{
									cols = lines[i + 5].split(" ");
									if ( cols.length < 10) {
										inc = inc + 1;
										itemName = itemName + " " + lines[i + 5];
									}
								}
							}
	
						}
						
						currLineCols = currLine.trim().split(" ");
						nextLineCols = nextLine.trim().split(" ");
						
						currLineCols[1] = currLineCols[1] + nextLineCols[1] + columnDelim + nextLineCols[0] + columnDelim + itemName;
						
						currLine = String.join(columnDelim, currLineCols);
						retPoData = retPoData + currLine + "\n";
						i=i+inc+3;
				}
			}catch(Exception e){
	        	log.error("Error in aligning PO Items:"+e.getMessage());
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
			return retPoData;
		}
	    


	private  List<PurchaseOrderItems> fetchPODeails(String poData) throws Exception{
		 List<PurchaseOrderItems> poDetails = new ArrayList<>();

		 String lines[];
		 try{
			 lines = poData.trim().split("\n"); // new line
			 
			 for (int i=0;i<lines.length;i++){
				 String cols[] =  lines[i].split(columnDelim);
				 PurchaseOrderItems poItem = new PurchaseOrderItems();
				 poItem.setItemNo((i+1));
				 
				 poItem.setEanNo(cols[0]);
				 poItem.setHsnNo(cols[1]);
				 poItem.setArticleNo(cols[2]);
				 poItem.setMaterialDesc( cols[3] ); 
				 poItem.setMrp(Float.parseFloat(cols[4].trim()));
				 poItem.setQuantity(Float.parseFloat(cols[5].trim()));
				 poItem.setUom(cols[6]);
				 poItem.setBasicCost(Float.parseFloat(cols[7].trim()));
				 poItem.setCgst(Float.parseFloat(cols[9].trim()));
				 poItem.setSgst(Float.parseFloat(cols[10].trim()));
				 poItem.setSalesTax(poItem.getCgst() + poItem.getSgst() );
				 poItem.setTotalBaseValue(Float.parseFloat(cols[11].trim().replaceAll(",","")));
				 poDetails.add(poItem);
			 }
			 //poDetails = removeDuplicateItems(poDetails);
		 }catch(Exception e){
			 log.error("Error getting PO Item details:"+e.getMessage());
			 throw new Exception(e.getMessage());
		 }
		 return poDetails;
	}
		

}
