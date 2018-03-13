
package com.takkaiah.pdf.read;


import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
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

public class SpencersPDF  {
	
	 POReaderLogger log = POReaderLogger.getLogger(ReliancePDF.class.getName());
	 PurchaseOrderInfo poInfo;
	 int docPages = 0;
	 final  String columnDelim = PDFReaderUtil.POColumnsDelimeter; 
	 boolean outStation = false;
	 public SpencersPDF(){

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
        	 throw new POFormatException("PO Format Error");
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
	    	  
              Rectangle rect1 = new Rectangle( 40, 10, 470, 10 );
              Rectangle rect2 = new Rectangle( 220,140, 500, 60 );
      
              stripper.addRegion( "customerName", rect1 );
              stripper.addRegion( "poInfo", rect2 );

                   
              stripper.extractRegions( firstPage );
              
              poInfo.setCustomerName(stripper.getTextForRegion( "customerName" ).trim());
              fetchPOInfo( stripper.getTextForRegion( "poInfo" ).trim());
  
              } catch(Exception e){
            	  log.error("Error getting PO header details:"+e.getMessage());
        	  throw new Exception(e.getMessage());
          }
  
	 }
	 
	
	    private  void fetchPOInfo(String poData) throws Exception{
	    	 String lines[];
			 try 
			 {
				 lines = poData.split("\n");
	        	 if(lines.length>0)
	        	 {
	        		 if (lines[0].contains("PO Number")==true){
	        			 String postr[] = lines[0].split(":");
	        			 if (postr.length>0)
	        				 poInfo.setPoNumber(postr[1].trim());
	        		 }
	         		 if (lines[1].contains("PO Date")==true){
	         			 String podt[] = lines[1].split(":");
	        			 if (podt.length>0) {
	         	  			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	  							poInfo.setPoDate(dateFormat.parse(podt[1].trim()));
		        			 }
	        		 }
	        		 if (lines[2].contains("Delivery Date")==true){
	         			 String podt[] = lines[2].split(":");
	        			 if (podt.length>0) {
	         	  			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
								poInfo.setDeliveryDate(dateFormat.parse(podt[1].trim()));
	        			 }
	        		 }
	        	  }	
				} catch (ParseException e) {
	            	  log.error("Error getting PO header details:"+e.getMessage());
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
		        	 pdfText = pdfText.replaceAll("-", "").trim();
		        	 if (i==poItemsStartPage ) {
			        	 if (pdfText.indexOf("IGST %") > 0) {
			        		 outStation = true;
			        		 poInfo.setOutStation(true);
			        	 } 
		        	 }
	        		 if (i==1) {
			        	 if (pdfText.indexOf("BUoM")>0) {
			        		 pdfText = pdfText.substring(pdfText.indexOf("BUoM")+14);
			        		 startPage = true;
			        	 }
	        		 } else {
			        	 if (pdfText.indexOf("www.spencersretail.com")>0) {
			        		 pdfText = pdfText.substring(pdfText.indexOf("www.spencersretail.com")+23);
			        		 startPage = true;
			        	 }
	        		 }
		        	 if (pdfText.indexOf("Total")>0){
		        		 pdfText = pdfText.substring(0, pdfText.indexOf("Total"));
		        		 endPage=true;
		        		 i = docPages + 1;
		        	 } else //if(true)   // Check for any end of the page string 
		        	 {
		        		 endPage=true;
		        	 }
		        	 if (startPage==true && endPage==true){
		        		 poLines = poLines + pdfText;
		        	 }
		        	 startPage=false;
		        	 endPage=false;
		 		}
		 		poLines = PDFReaderUtil.removeEmptyLines(poLines);
		 		poLines = alignRows(poLines);
	           	List<PurchaseOrderItems> poItems = fetchPODeails(poLines);
	           	poInfo.setPoItems(poItems);
	        } catch (Exception e) {
	        	log.error("Error getting PO Item details :"+e.getMessage());
	            throw new Exception(e.getMessage());
	        } 
	    }
	    
	 	    
	
		// This method will align the scrambled row     
		private String alignRows(String poData) throws Exception{
			String retPoData = "";
			String lines[];
			String itemName ="";
			String currLine ="";
			String nextLine1 ="";
			String nextLine2 = "";
			int itemNameColNumber = 9;
			if (outStation == true) {
				itemNameColNumber = 8;
			}
			try{
				lines = poData.trim().split("\n"); 
				for (int i=0;i<lines.length;i++)
				{
					try{
						itemName ="";
						currLine = PDFReaderUtil.removeExtraSpaces(lines[i]);
						nextLine1 = PDFReaderUtil.removeExtraSpaces(lines[i+1]);
						nextLine2 = PDFReaderUtil.removeExtraSpaces(lines[i+2]);
						String currLineCols[] = currLine.trim().split(" ");
						String nextLineCols[] = null;
						for (int k=2;k< currLineCols.length-itemNameColNumber;k++ ) {
							itemName = itemName + currLineCols[k] + " ";
							currLineCols[k] = "";
						}
						if ( currLine.indexOf("/F") >= 0 ) {
							nextLineCols = nextLine1.trim().split(" ");
						} else {
							itemName = itemName + nextLine1;
							nextLineCols = nextLine2.trim().split(" ");
							i=i+1;
						}
						itemName = itemName.substring(0, itemName.indexOf("/F"));
						itemName = itemName + columnDelim + nextLineCols[0] + columnDelim + nextLineCols[1] + columnDelim + nextLineCols[nextLineCols.length-2] +  columnDelim + nextLineCols[nextLineCols.length-1];
						currLine = String.join(" ", currLineCols);
						currLine = PDFReaderUtil.removeExtraSpaces(currLine);
						currLineCols = currLine.trim().split(" ");
						currLineCols[1] = currLineCols[1] + columnDelim + itemName;
						currLine = String.join(columnDelim, currLineCols);
						retPoData = retPoData + currLine + "\n";
						if (outStation == true) {
							i++;
						}
						i++;
					}catch(NumberFormatException nfe){
					}
				}
			}catch(Exception e){
	        	log.error("Error in aligning PO Items:"+e.getMessage());
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
			return retPoData;
		}
	    

		private  List<PurchaseOrderItems> fetchPODeails(String poData) throws Exception {
			List<PurchaseOrderItems> poDetails = new ArrayList<>();
			String lines[];
			try{
				 lines = poData.trim().split("\n"); // new line
			 
				 for (int i=0;i<lines.length;i++){
					 String cols[] =  lines[i].split(columnDelim);
					 PurchaseOrderItems poItem = new PurchaseOrderItems();
					 poItem.setItemNo(Integer.parseInt(cols[0]));
					 poItem.setArticleNo(cols[1]);
					 poItem.setMaterialDesc( cols[2] ); 
					 poItem.setHsnNo(cols[3]);
					 poItem.setMrp(Float.parseFloat(cols[4].trim()));
					 poItem.setBasicCost(Float.parseFloat(cols[5].trim().replaceAll(",","")));
					 poItem.setTotalBaseValue(Float.parseFloat(cols[6].trim().replaceAll(",","")));
					 poItem.setEanNo(cols[7]);
					 poItem.setQuantity(Float.parseFloat(cols[cols.length-2].trim()));
					 poItem.setUom(cols[cols.length-1]);
					 
					 if (outStation==true) {
						 float tax = Float.parseFloat(cols[10].trim());
						 poItem.setCgst((tax/2));
						 poItem.setSgst((tax/2));
					 } else {
						 poItem.setCgst(Float.parseFloat(cols[10].trim()));
						 poItem.setSgst(Float.parseFloat(cols[11].trim()));
					 }
					 poItem.setSalesTax(poItem.getSgst() + poItem.getCgst());
					 
					 poDetails.add(poItem);
				 }
			 }catch(Exception e){
	        	 log.error("Error getting PO Items into PO Object:"+e.getMessage());
	        	 throw new Exception(e.getMessage());
			 }
			 return poDetails;
		}
}
