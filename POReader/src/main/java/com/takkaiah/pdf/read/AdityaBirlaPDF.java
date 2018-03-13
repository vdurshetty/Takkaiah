
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

	public class AdityaBirlaPDF  {
		
		POReaderLogger log = POReaderLogger.getLogger(AdityaBirlaPDF.class.getName());
		PurchaseOrderInfo poInfo;
		 int docPages = 0;
		 final String columnDelim = PDFReaderUtil.POColumnsDelimeter;
		 public AdityaBirlaPDF(){

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
	        	 log.error("Error getting PO Details:"+e.getMessage());
				throw new POFormatException("PO Format Error");
	         } finally{
	        	  if( document != null )
		          {
		                  try {
							document.close();
						} catch (IOException e) {
				        	 log.error("Error closing PDF Document :"+e.getMessage());
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
		    	  
	              Rectangle rect1 = new Rectangle( 10, 10, 800, 50 );
	              Rectangle rect2 = new Rectangle( 500,90, 250, 20 );
	              Rectangle rect3 = new Rectangle( 270,220, 200, 60 );
	      
	              stripper.addRegion( "customerName", rect1 );
	              stripper.addRegion( "poNo", rect2 );
	              stripper.addRegion( "poInfo", rect3 );

		                   
	              stripper.extractRegions( firstPage );
	              
	              String cols[] = stripper.getTextForRegion( "poNo" ).trim().split(":");
	              
	              poInfo.setCustomerName(stripper.getTextForRegion( "customerName" ).trim());
	              poInfo.setPoNumber(cols[1].trim());
	              fetchPOInfo( stripper.getTextForRegion( "poInfo" ).trim());
	  
	              } catch(Exception e){
	 	        	 log.error("Error getting PO Header Details:"+e.getMessage());
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
					        	 log.error("Error getting PO Date :"+e.getMessage());
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
					        	 log.error("Error getting PO Delivery Date:"+e.getMessage());
								throw new Exception(e.getMessage());
							}
	        			 }
	        		 }
	        		 if (lines[2].contains("Expiry Date")==true){
	         			 String poExpDt[] = lines[2].split(":");
	        			 if (poExpDt.length>0) {
	         	  			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
	        				try {
								poInfo.setPoExpiryDate(dateFormat.parse(poExpDt[1].trim()));
							} catch (ParseException e) {
					        	 log.error("Error getting PO Expiry Date:"+e.getMessage());
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
			        	 stripper.setStartPage(i);
			        	 stripper.setEndPage(i);
			        	 String pdfText = stripper.getText(document);
			        	// System.out.println(" Line :"+ i + " - " + pdfText);
			        	 if (pdfText.indexOf("Type% (INR) (INR)")>0) {
			        		 pdfText = pdfText.substring(pdfText.indexOf("Type% (INR) (INR)")+18);
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
			 		poLines = alignRows(poLines);
			 		//System.out.println("\n\nVenuGopal :" + poLines);
		           	List<PurchaseOrderItems> poItems = fetchPODeails(poLines);
		           	poInfo.setPoItems(poItems);
		        } catch (Exception e) {
		        	 log.error("Error getting PO Items:"+e.getMessage());
		            throw new Exception(e.getMessage());
		        } 
		        
		    }
		
		
		// This method will align the scrambled row     
		private String alignRows(String poData) throws Exception{
			String retPoData = "";
			int itemNum = 1;
			String lines[];
			String itemName ="";
			String itemNameExt = "";
			String preLine ="";
			String currLine ="";
			String nextLine ="";
			String gst = "";
			String sgst ="";

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
								preLine = PDFReaderUtil.removeExtraSpaces(lines[i-1]);
								currLine = PDFReaderUtil.removeExtraSpaces(lines[i]);
								nextLine = PDFReaderUtil.removeExtraSpaces(lines[i+1]);
								itemName ="";
								itemNameExt = "";
								
								if (currLine.indexOf("SGST/UTGST") > 0 ) { 
									itemNameExt = nextLine.trim();
									nextLine = currLine.substring(currLine.indexOf("SGST/UTGST")).trim();
									currLine = currLine.substring(0,currLine.indexOf("SGST/UTGST") ).trim();
									String curCols[] = currLine.split(" ");
									for (int k=3;k< curCols.length-5;k++ ) {
										itemName = itemName + curCols[k] + " ";
										curCols[k] = "";
									}
									currLine = String.join(" ", curCols);
									currLine = PDFReaderUtil.removeExtraSpaces(currLine);
									currLine = currLine + " " + nextLine.substring(nextLine.lastIndexOf(" ")).trim();
								} else if (currLine.indexOf("CGST") > 0 ) {
									itemNameExt = nextLine.trim();
									nextLine = currLine.substring(currLine.indexOf("CGST")).trim();
									currLine = currLine.substring(0,currLine.indexOf("CGST")).trim();
									String curCols[] = currLine.split(" ");
									for (int k=3;k< curCols.length-5;k++ ) {
										itemName = itemName + curCols[k] + " ";
										curCols[k] = "";
									}
									currLine = String.join(" ", curCols);
									currLine = PDFReaderUtil.removeExtraSpaces(currLine);
									currLine = currLine + " " + nextLine.substring(nextLine.lastIndexOf(" ")).trim();
								}
								if (preLine.indexOf("CGST") > 0 ) 	{
									itemName =  preLine.substring(0, preLine.indexOf("CGST")).trim() + " " + itemName;
								} else {
									itemName = preLine.substring(0, preLine.indexOf("SGST/UTGST")).trim() + " " + itemName;
								}

								if (nextLine.indexOf("CGST") > 0 ) 	{
									itemName = itemName.trim() + nextLine.substring(0, nextLine.indexOf("CGST")) + " " + itemNameExt;
								} else {
									itemName = itemName.trim() + nextLine.substring(0, nextLine.indexOf("SGST/UTGST")) + " " + itemNameExt;
								}
								
								String preLineCols[] = preLine.trim().split(" ");
								String nextLineCols[] = nextLine.trim().split(" ");
								gst = preLineCols[preLineCols.length-2].replaceAll("%", "");
								sgst = nextLineCols[nextLineCols.length-2].replaceAll("%", ""); 
								String curLineCols[] = currLine.trim().split(" ");
								curLineCols[curLineCols.length-7] = curLineCols[curLineCols.length-7] + columnDelim + itemName.trim() + columnDelim + gst + columnDelim + sgst;
				
								currLine = String.join(columnDelim, curLineCols);
								//System.out.println("CurrLine :"+ currLine);
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
					 //System.out.println("Line " + i + " - " + lines[i]);
					 String cols[] =  lines[i].split(columnDelim);
					 PurchaseOrderItems poItem = new PurchaseOrderItems();
					 poItem.setItemNo(Integer.parseInt(cols[0]));
					 poItem.setHsnNo(cols[1]);
					 poItem.setArticleNo(cols[2]);
					 poItem.setMaterialDesc( cols[3] ); 
					 poItem.setCgst(Float.parseFloat(cols[4].trim()));
					 poItem.setSgst(Float.parseFloat(cols[5].trim()));
					 poItem.setEanNo(cols[6].trim());
					 poItem.setMrp(Float.parseFloat(cols[7].trim()));
					 poItem.setBasicCost(Float.parseFloat(cols[8].trim()));
					 poItem.setUom(cols[9].trim());
					 poItem.setQuantity(Float.parseFloat(cols[10].trim()));
					 poItem.setTotalBaseValue(Float.parseFloat(cols[11].trim().replaceAll(",","")));
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
