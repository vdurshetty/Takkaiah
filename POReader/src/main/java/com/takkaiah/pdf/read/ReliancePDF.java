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

public class ReliancePDF  {
	
	POReaderLogger log = POReaderLogger.getLogger(ReliancePDF.class.getName());
	 PurchaseOrderInfo poInfo;
	 int docPages = 0;
	 final  String columnDelim = PDFReaderUtil.POColumnsDelimeter; 
	 boolean outStation = false;
	 public ReliancePDF(){
		 	
	 }
	 
	

	public PurchaseOrderInfo fetchPODetails(String poFile) throws POFormatException {
		PDDocument document = null;
		 try
         {
			 poInfo = new PurchaseOrderInfo();
			 document  = PDDocument.load( new File(poFile) );  // Add exception like , pdfFile not found, unable to read pdf File
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
						throw new POFormatException(e.getMessage());
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
	    	  
              Rectangle rect1 = new Rectangle( 10, 10, 200, 10 );
              Rectangle rect2 = new Rectangle( 300, 110, 400, 80 );
              Rectangle rect3 = new Rectangle( 10, 600,250, 50 );
 
              stripper.addRegion( "customerName", rect1 );
              stripper.addRegion( "poInfo", rect2 );
              stripper.addRegion( "deliveryDate", rect3 );
                 
              stripper.extractRegions( firstPage );
               
              poInfo.setCustomerName(stripper.getTextForRegion( "customerName" ).trim());
              fetchPOInfo( stripper.getTextForRegion( "poInfo" ).trim());
              fetchDeliveryDate( stripper.getTextForRegion( "deliveryDate").trim());

              } catch(Exception e){
            	  log.error("Error getting PO Header details:"+e.getMessage());
        	  throw new Exception(e.getMessage());
          }
  
	 }
	 
	
	    private  void fetchPOInfo(String poData) throws Exception{
	        String lines[];
			try {
				 lines =  poData.split("\n");
	        	 if(lines.length>0){
	         		 if (lines[0].contains("Purchase Order")==true){
	        			 String postr[] = lines[0].split(":");
	        			 if (postr.length>0)
	        				 poInfo.setPoNumber(postr[1].trim());
	        		 }
	        		 if (lines[1].contains("Date")==true)
	        		 {
	         			 String podt[] = lines[1].split(":");
	        			 if (podt.length>0) {
	         	  			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
								poInfo.setPoDate(dateFormat.parse(podt[1].trim()));
	        			 }
	        		 }
	        	 }
				} catch (Exception e) {
					log.error("Error getting PO Header details:"+e.getMessage());
					throw new Exception(e.getMessage());
				}
          }

	    private  void fetchDeliveryDate(String dData) throws Exception{
		    	DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				try {
					poInfo.setDeliveryDate(dateFormat.parse(dData.substring(dData.indexOf("DELIVERY DATE")+14).trim()));
				} catch (ParseException e) {
					log.error("Error getting PO delivery date:"+e.getMessage());
					throw new Exception(e.getMessage());
				}
	    } 

		    
	    private void fetchPORows(PDDocument document) throws Exception{
	        try {
		 		int poItemsStartPage = 2;
		 		String poLines = "";
		 		boolean startPage = false;
		 		boolean endPage = false;
		 		for  (int i= poItemsStartPage;i <= docPages;i++ ) {
		        	 PDFTextStripper stripper = new PDFTextStripper();
		        	 stripper.setSortByPosition( true );
		        	 stripper.setStartPage( i );
		        	 stripper.setEndPage(i);
		        	 String pdfText = stripper.getText(document);
		        	 pdfText = pdfText.replaceAll("_", "").trim();
		        	 if (i==poItemsStartPage ) {
			        	 if (pdfText.indexOf("IGST(%)") > 0) {
			        		 outStation = true;
			        		 poInfo.setOutStation(true);
			        	 } 
			         }
		        	 if (outStation == true) {
			        	 if (pdfText.indexOf("Site")>0) {
			        		 pdfText = pdfText.substring(pdfText.indexOf("Site")+4);
			        		 startPage = true;
			        	 }
		        	 } else {
			        	 if (pdfText.indexOf("CESS(INR)")>0) {
			        		 pdfText = pdfText.substring(pdfText.indexOf("CESS(INR)")+9);
			        		 startPage = true;
			        	 }
		 			 }	
		        	 
		        	 if (pdfText.indexOf("Grand Total")>0){
		        		 pdfText = pdfText.substring(0, pdfText.indexOf("Grand Total"));
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
			int itemNum = 1;
			String lines[];
			String itemName ="";
			String currLine ="";
			String nextLine1 ="";
			//String nextLine2 = "";
			String nextLine3 ="";
			int firstColValue = -1;
			try{
				lines = poData.trim().split("\n"); 
				for (int i=0;i<lines.length;i++)
				{
					
					String cols[] = lines[i].trim().split(" ");
					try{
						firstColValue = Integer.parseInt(cols[0]);
						if ( firstColValue==itemNum)
						{
								itemNum++;
								currLine = PDFReaderUtil.removeExtraSpaces(lines[i]);
								nextLine1 = PDFReaderUtil.removeExtraSpaces(lines[i+1]);
								//nextLine2 = PDFReaderUtil.removeExtraSpaces(lines[i+2]);
								nextLine3 = PDFReaderUtil.removeExtraSpaces(lines[i+3]);
								
								itemName ="";
								String currLineCols[] = currLine.trim().split(" ");
								for (int k=3;k< currLineCols.length-7;k++ ) {
									itemName = itemName + currLineCols[k] + " ";
									currLineCols[k] = "";
								}
								currLine = String.join(" ", currLineCols);
								currLine = PDFReaderUtil.removeExtraSpaces(currLine);
								
								currLineCols = currLine.trim().split(" ");
								String nextLine1Cols[] = nextLine1.trim().split(" ");
								String nextLine3Cols[] = nextLine3.trim().split(" ");
								itemName = itemName.trim() + columnDelim + nextLine1Cols[0]; 
		
								currLineCols[3] = currLineCols[3] + columnDelim + nextLine3Cols[0] ; currLineCols[4] = nextLine3Cols[1]; currLineCols[5] = nextLine3Cols[2];
								if (outStation == false) {
									currLineCols[7] = currLineCols[7] + columnDelim + nextLine1Cols[1];
								} else {
									float taxVal =  Float.parseFloat(currLineCols[7].trim());  
									currLineCols[7] = (taxVal/2) + columnDelim + (taxVal/2);
								}
								
								currLineCols[2] = currLineCols[2] + columnDelim + itemName;

								
								currLine = String.join(columnDelim, currLineCols);
								retPoData = retPoData + currLine + "\n";
							} 
					}catch(NumberFormatException nfe){
						  //nfe.printStackTrace();
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
				 poItem.setEanNo(cols[2]);
				 poItem.setMaterialDesc( cols[3] ); 
				 poItem.setHsnNo(cols[4]);
				 poItem.setcQty(Float.parseFloat(cols[5].trim()) );
				 poItem.setQuantity(Float.parseFloat(cols[6].trim()));
				 poItem.setUom(cols[7].trim());
				 poItem.setMrp(Float.parseFloat(cols[8].trim()));
				 poItem.setBasicCost(Float.parseFloat(cols[9].trim().replaceAll(",","")));
				 poItem.setCgst(Float.parseFloat(cols[10].trim()));
				 poItem.setSgst(Float.parseFloat(cols[11].trim()));
				 
				 poItem.setTotalBaseValue(Float.parseFloat(cols[13].trim().replaceAll(",","")));
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
