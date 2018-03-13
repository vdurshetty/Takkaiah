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

public class HyperCityPDF {
	
	 POReaderLogger log = POReaderLogger.getLogger(HyperCityPDF.class.getName());
	 PurchaseOrderInfo poInfo;
	 int docPages = 0;
	 final  String columnDelim = PDFReaderUtil.POColumnsDelimeter; 
	 public HyperCityPDF(){

	 }

	public PurchaseOrderInfo getPODetails(String poFile) throws POFormatException {
		PDDocument document = null;
		 try
         {
			 poInfo = new PurchaseOrderInfo();
			 document  = PDDocument.load( new File(poFile) );
	         docPages = document.getNumberOfPages();
	         fetchPOHeader(document);
	         fetchPOrows(document);
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
	    	  
              Rectangle rect1 = new Rectangle( 40, 20, 400, 20 );
              Rectangle rect2 = new Rectangle( 0,40, 200, 20 );
              Rectangle rect3 = new Rectangle( 550,0, 200, 100 );
      
              stripper.addRegion( "customerName", rect1 );
              stripper.addRegion( "poNumber", rect2 );
              stripper.addRegion( "poInfo", rect3 );

                   
              stripper.extractRegions( firstPage );
               
              poInfo.setCustomerName(stripper.getTextForRegion( "customerName" ).trim());
              String poStr[] = stripper.getTextForRegion( "poNumber").trim().split("\n");
              String poNum[] = poStr[0].trim().split(":");
              poInfo.setPoNumber(poNum[1].trim().replaceAll(" ", ""));
              
              
              fetchPOInfo( stripper.getTextForRegion( "poInfo" ).trim());
  
          } catch(Exception e){
        	  log.error("Error getting PO Header details:"+e.getMessage());
        	  throw new Exception(e.getMessage());
          }
	 }
	 
	
	    private  void fetchPOInfo(String poData) throws Exception
	    {
	    	String lines[];
			try {
				 lines	= poData.split("\n");
	        	 if(lines.length>0)
	        	 {
	         		 if (lines[0].contains("Date")==true)
	         		 {
	         			 String podt[] = lines[0].split(":");
	        			 if (podt.length>0) {
	         	  			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
	    					poInfo.setPoDate(dateFormat.parse(podt[1].trim()));
		        		 }
	        		 }
	        		 if (lines[4].contains("Ship Date")==true)
	        		 {
	         			 String podt[] = lines[4].split("-");
	        			 if (podt.length>0) {
	         	  			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
							poInfo.setDeliveryDate(dateFormat.parse(podt[1].trim()));
		        		 }
	        		 }
	           		 if (lines[5].contains("Cancel Date")==true)
	        		 {
	         			 String poExpDt[] = lines[5].split("-");
	        			 if (poExpDt.length>0) {
	         	  			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
							poInfo.setPoExpiryDate(dateFormat.parse(poExpDt[1].trim()));
		        		 }
	        		 }
	
	          	 }
	        	} catch (ParseException e) {
	        		log.error("Error getting PO Header details:"+e.getMessage());
					throw new Exception(e.getMessage());
				}
        }

        
	    
	    private void fetchPOrows(PDDocument document) throws Exception{
	        try {
	  	 		int poItemsStartPage = 1;
		 		String poLines = "";
		 		boolean pageStart = false;
		 		boolean pageEnd = false;
		 		for  (int i= poItemsStartPage;i <= docPages;i++ ) {
		        	 PDFTextStripper stripper = new PDFTextStripper();
		        	 stripper.setSortByPosition( true );
		        	 stripper.setStartPage( i );
		        	 stripper.setEndPage(i);
		        	 String pdfText = stripper.getText(document);
		        	 if ( pdfText.indexOf("Val / %") >0 ){
		        		 pdfText = pdfText.substring(pdfText.indexOf("Val / %")+8);
		        		 pageStart=true;
		        	 }
	        		 if (pdfText.indexOf("/Cont.")>0){
		        		 pdfText = pdfText.substring(0, pdfText.indexOf("/Cont.")-1);
		        		 pageEnd=true;
		        		 
	        		 } else if ( pdfText.indexOf("Sub Total")>0){
	        			 pdfText = pdfText.substring(0, pdfText.indexOf("Sub Total"));
	        			 pageEnd=true;
	        		 }
	        		 if (pageStart==true && pageEnd==true){
	        			 poLines = poLines + pdfText;
	        		 }
		        	 pageStart = false;
		        	 pageEnd=false;
		 		}
		 		poLines = PDFReaderUtil.removeEmptyLines(poLines);
		 		poLines = alignRows(poLines);
	  	        List<PurchaseOrderItems> poItems = fetchPODeails(poLines);
	           	poInfo.setPoItems(poItems);
	        	 
	        } catch (IOException e) {
	        	log.error("Error getting PO Item details:"+e.getMessage());
	            throw new Exception(e.getMessage());
	        } 
	    }
	    
		
	// This method will align the scrambled row     
	private String alignRows(String poData) throws Exception{
		String retPoData = "";
		String lines[];
		String cols[];
		String nextLineCols[];
		String itemName ="";
		String hsnLine = "";
		String cgstLine = "";
		String sgstLine ="";
		try{
			lines = poData.trim().split("\n"); 
			for (int i=0;i<lines.length;i++)
			{
				String lineData =  PDFReaderUtil.removeExtraSpaces(lines[i]);
				//System.out.println("Line :" + (i+1) + " - :" + lineData  );
				cols = lineData.trim().split(" ");
				//System.out.println("Cols :" + cols.length + " - Line :" + (i+1) + " - :" + lineData  );
				itemName ="";
				for (int k=1;k< cols.length-11;k++ ) {
					itemName = itemName + cols[k] + " ";
					cols[k] = "";
				}
				itemName = cols[0] + columnDelim + itemName.trim();
				hsnLine = PDFReaderUtil.removeExtraSpaces(lines[i+1]);
				cgstLine = PDFReaderUtil.removeExtraSpaces(lines[i+2]); 
				sgstLine = PDFReaderUtil.removeExtraSpaces(lines[i+3]); 
				
				nextLineCols = hsnLine.trim().split(" ");
				itemName = itemName.trim() + columnDelim + nextLineCols[0];
				
				nextLineCols = cgstLine.trim().split(" ");
				itemName = itemName.trim() + columnDelim + nextLineCols[4];
				
				nextLineCols = sgstLine.trim().split(" ");
				itemName = itemName.trim() + columnDelim + nextLineCols[4];

				lineData = lineData.substring(lineData.indexOf(".")+1).trim();
				lineData = lineData.replace(" ", columnDelim);
				lineData = itemName + columnDelim + lineData;
				i = i+3;
				retPoData = retPoData + lineData + "\n";
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
				 poItem.setItemNo((i+1));
				 
				 poItem.setArticleNo(cols[0]);
				 poItem.setMaterialDesc( cols[1] ); 
				
				 poItem.setHsnNo(cols[2]);
				 poItem.setCgst(Float.parseFloat(cols[3].trim()));
				 poItem.setSgst(Float.parseFloat(cols[4].trim()));
				 poItem.setUom(cols[5]);
				 poItem.setMrp(Float.parseFloat(cols[7].trim()));
				 poItem.setBasicCost(Float.parseFloat(cols[8].trim()));
				 poItem.setQuantity(Float.parseFloat(cols[11].trim()));
				 poItem.setEanNo("-");
				 poItem.setTotalBaseValue(Float.parseFloat(cols[12].trim().replaceAll(",","")));
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
