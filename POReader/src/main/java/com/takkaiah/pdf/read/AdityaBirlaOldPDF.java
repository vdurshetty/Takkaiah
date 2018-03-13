
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

	public class AdityaBirlaOldPDF  {
		
		POReaderLogger log = POReaderLogger.getLogger(AdityaBirlaOldPDF.class.getName());
		 PurchaseOrderInfo poInfo;
		 int docPages = 0;
	
		 public AdityaBirlaOldPDF(){

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
	     		log.error("Error in getPO Details :" + e.getMessage());
	        	 throw new POFormatException("PO Format Error");
	         } finally{
	        	  if( document != null )
		          {
		                  try {
							document.close();
						} catch (IOException e) {
				     		log.error("Error in Closing pdf document :" + e.getMessage());
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
		    	  
	              Rectangle rect1 = new Rectangle( 10, 10, 800, 20 );
	              Rectangle rect2 = new Rectangle( 500,60, 150, 20 );
	              Rectangle rect3 = new Rectangle( 250,180, 200, 60 );
	      
	              stripper.addRegion( "customerName", rect1 );
	              stripper.addRegion( "poNo", rect2 );
	              stripper.addRegion( "poInfo", rect3 );

	                   
	              stripper.extractRegions( firstPage );
	              
	              String cols[] = stripper.getTextForRegion( "poNo" ).trim().split(":");
	              
	              poInfo.setPoNumber(cols[1].trim());
	              poInfo.setCustomerName(stripper.getTextForRegion( "customerName" ).trim());
	              fetchPOInfo( stripper.getTextForRegion( "poInfo" ).trim());
	  
	              } catch(Exception e){
	           		log.error("Error fetching header details :" + e.getMessage());
	        	  throw new Exception(e.getMessage());
	          }
	  
		 }
		 
		
		    private  void fetchPOInfo(String poData) throws Exception{
		    	 String lines[] = poData.split("\n");
	        	 if(lines.length>0){
	         		 if (lines[0].contains("PO Date")==true){
	         			 String podt[] = lines[0].split(":");
	        			 if (podt.length>0) {
	         	  			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
	        				try {
								poInfo.setPoDate(dateFormat.parse(podt[1].trim()));
							} catch (ParseException e) {
					     		log.error("Error in reading PO Date :" + e.getMessage());
								throw new Exception(e.getMessage());
							}
	        			 }
	        		 }
	        		 if (lines[1].contains("Delivery Date")==true){
	         			 String podt[] = lines[1].split(":");
	        			 if (podt.length>0) {
	         	  			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
	        				try {
								poInfo.setDeliveryDate(dateFormat.parse(podt[1].trim()));
							} catch (ParseException e) {
					     		log.error("Error reading PO Delivery Date :" + e.getMessage());
								throw new Exception(e.getMessage());
							}
	        			 }
	        		 }
	        		 if (lines[2].contains("Expiry Date")==true){
	         			 String poexpDt[] = lines[2].split(":");
	        			 if (poexpDt.length>0) {
	         	  			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
	        				try {
								poInfo.setPoExpiryDate(dateFormat.parse(poexpDt[1].trim()));
							} catch (ParseException e) {
					     		log.error("Error in reading PO expiry date :" + e.getMessage());
								throw new Exception(e.getMessage());
							}
	        			 }
	        		 }
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
			        	 
			        	 //pdfText = pdfText.replaceAll("_", "").trim();
			        	 if (pdfText.indexOf("(excl VAT)")>0) {
			        		 pdfText = pdfText.substring(pdfText.indexOf("(excl VAT)")+11);
			        		 startPage = true;
			        	 }
			        	 if (pdfText.indexOf("Total Qty:")>0){
			        		 pdfText = pdfText.substring(0, pdfText.indexOf("Total Qty:"));
			        		 endPage=true;
			        	 } else if(pdfText.indexOf("Page")>0)   
			        	 {
			        		 pdfText = pdfText.substring(0, pdfText.indexOf("Page"));
			        		 endPage=true;
			        	 }

			        	 if (startPage==true && endPage==true){
			        		 poLines = poLines + pdfText;
			        	 }
			        	 startPage=false;
			        	 endPage=false;
			 		}
			 		poLines = PDFReaderUtil.removeEmptyLines(poLines);
			 		//poLines = alignRows(poLines);
		           	List<PurchaseOrderItems> poItems = fetchPODeails(poLines);
		           	poInfo.setPoItems(poItems);
		        } catch (Exception e) {
		     		log.error("Error reading PO items :" + e.getMessage());
		            throw new Exception(e.getMessage());
		        } 
		        
		    }
		    
	

		private  List<PurchaseOrderItems> fetchPODeails(String poData) throws Exception {
			List<PurchaseOrderItems> poDetails = new ArrayList<>();
			String lines[];
			try{
				 lines = poData.trim().split("\n"); // new line
			 
				 for (int i=0;i<lines.length;i++){
 					 String cols[] =  PDFReaderUtil.removeExtraSpaces(lines[i]).split(" "); //lines[i].split(" ");
					 PurchaseOrderItems poItem = new PurchaseOrderItems();
					 poItem.setItemNo(Integer.parseInt(cols[0]));
					 poItem.setArticleNo(cols[1]);
					 poItem.setTotalBaseValue(Float.parseFloat(cols[cols.length - 1].trim().replaceAll(",","")));
					 poItem.setSalesTax(Float.parseFloat(cols[cols.length - 3].trim()));
					 poItem.setQuantity(Float.parseFloat(cols[cols.length - 4].trim()));
					 poItem.setUom(cols[cols.length - 5].trim());
					 poItem.setBasicCost(Float.parseFloat(cols[cols.length - 6].trim()));
					 poItem.setMrp(Float.parseFloat(cols[cols.length - 7].trim()));
					 poItem.setEanNo(cols[cols.length - 8].trim());
					 String materialDesc = "";
					 for (int j=9;j<cols.length-1;j++){
						 materialDesc= materialDesc + cols[cols.length - j].trim() + " ";
					 }
					 poItem.setMaterialDesc( PDFReaderUtil.reverseString(materialDesc) ); 
					 poDetails.add(poItem);
				 }
			 }catch(Exception e){
		     	 log.error("Error reading PO items into PO Object :" + e.getMessage());
				 throw new Exception(e.getMessage());
			 }
			 return poDetails;
		}
			
	
	}
