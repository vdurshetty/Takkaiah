
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

public class MaxHypermarketPDF  {
	
	 POReaderLogger log = POReaderLogger.getLogger(MaxHypermarketPDF.class.getName());
	 PurchaseOrderInfo poInfo;
	 int docPages = 0;
	 final String columnDelim = PDFReaderUtil.POColumnsDelimeter;
	 public MaxHypermarketPDF(){

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
	    	  
              Rectangle rect1 = new Rectangle( 20, 10, 400, 10 );
              Rectangle rect2 = new Rectangle( 240,100, 170, 120 );
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
	         		 if (lines[0].toUpperCase().contains("PO NO")==true)
	         		 {
	        			 String postr[] = lines[0].split(":");
	        			 if (postr.length>0)
	        				 poInfo.setPoNumber(postr[1].trim());
	        		 }
	         		 if (lines[1].toUpperCase().contains("PO DATE")==true)
	         		 {
	         			 String podt[] = lines[1].split(":");
	         			 if (podt.length>0) {
	         				 DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
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
			        	// System.out.println(pdfText);
			        	 
			        	 if (pdfText.indexOf("HSN Cost")>0) {
			        		 pdfText = pdfText.substring(pdfText.indexOf("HSN Cost") + 8);
			        	 //if (pdfText.indexOf("Amount Value")>0) {
				        	//	 pdfText = pdfText.substring(pdfText.indexOf("Amount Value") + 14);
			                 startPage = true;
			        	 } else if (pdfText.indexOf("TIN:")>0){
			        		 pdfText = pdfText.substring(pdfText.indexOf("TIN:") + 17); 
			        		 startPage = true;
			        	 }
			        	 
			        	 if (pdfText.indexOf("Total:")>0){
			        		 pdfText = pdfText.substring(0, pdfText.indexOf("Total:"));
			        		 endPage=true;
			        		 i= docPages+2;  // End looping the pages if Total reached.
			        	 } else if(pdfText.indexOf("Location:")>0)   
			        	 {
			        		 pdfText = pdfText.substring(0, pdfText.indexOf("Location:"));
			        		 endPage=true;
			        	 }

			        	 if (startPage==true && endPage==true){
			        		 poLines = poLines + pdfText;
			        	 }
			        	 startPage=false;
			        	 endPage=false;
			 		}
			 		
			 		System.out.println(poLines + " \n\n");
			 		poLines = PDFReaderUtil.removeEmptyLines(poLines);
			 		System.out.println(poLines);
			 		
			 		poLines = alignRows(poLines);
			 		System.out.println(poLines);
		           	//List<PurchaseOrderItems> poItems = fetchPODeails(poLines);
		           	//poInfo.setPoItems(poItems);
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
			String firstLine = "";
			String secondLine = "";
			String thirdLine = "";
			String fourthLine = "";
			String fifthLine ="";
			String sixthLine = "";
			String lineData ="";
			int SKUCode;
			String secondLinecols[];
			String thirdLineCols[];
			String fourthLineCols[];
			String fifthLineCols[];
			String sgst ="";
			String cgst ="";
			String totalAmout ="";
			String unitCost ="";
			String mrp ="";
			String qty ="";
			String ean ="";
			String hsn ="";
			try{
				lines = poData.trim().split("\n"); 
				System.out.println("Total Lines :"+ lines.length);
				for (int i=0;i<lines.length;i++)
				{
					try {
						//System.out.println("before If  Line :" + i + " -" +lines[i]);
						 SKUCode = Integer.parseInt(lines[i].trim()); 
							//System.out.println("before If  Line :" + i + " -" +SKUCode);
						if (SKUCode > 0) {
							lineData ="";
							itemName = "";
							fifthLine ="";
							sixthLine ="";
							
							firstLine = lines[i];	secondLine = lines [i+1]; thirdLine = lines[i+2]; fourthLine = lines[i+3]; 
							// System.out.println(" 1  Line :" + i + " -" +lines[i]);
							//System.out.println(" 2 Line :" + (i+1) + " -" +lines[i+1]);
							// System.out.println(" 3 Line :" + (i+2) + " -" +lines[i+2]);
							// System.out.println(" 4 Line :" + (i+3) + " -" +lines[i+3]);
							if ( (i+4) <= lines.length) {
								fifthLine = lines[i+4];
								//System.out.println(" 5 Line :" + (i+4) + " -" +lines[i+4]);
								try {
									if (Integer.parseInt(fifthLine) > 0) {
										i = i + 1;
									} 
								} catch(Exception er) {
									
								}
							} 
							if ( (i+5) < lines.length ) {
								sixthLine = lines[i+5];
								//System.out.println(" 6 Line :" + (i+5) + " -" +lines[i+5]);
								try {
									if (Integer.parseInt(sixthLine) > 0) {
										i = i + 1;
									} 
								} catch(Exception er) {
									
								}
							}
							
							
							if (secondLine.indexOf("*") >= 0) {
								secondLine = secondLine.replace("*", "").trim();
								secondLinecols = secondLine.split(" ");
								for (int k=0;k< secondLinecols.length-1;k++ ) {
									itemName = itemName + secondLinecols[k] + " ";
								}
								// EAN Code
								ean = secondLinecols[secondLinecols.length-1];  	
								//System.out.println("Item Name :" + itemName);
								thirdLineCols = thirdLine.split(" ");
							} else if (thirdLine.indexOf("*") >= 0) {
								
								secondLinecols = secondLine.split(" ");
								for (int k=0;k< secondLinecols.length-1;k++ ) {
									itemName = itemName + secondLinecols[k] + " ";
								}
								//System.out.println("Item Name :" + itemName);
								// EAN Code
								ean = secondLinecols[secondLinecols.length-1];  	
								
								thirdLine = thirdLine.replace("*", "");
								thirdLine = PDFReaderUtil.removeExtraSpaces(thirdLine); 
								thirdLineCols = thirdLine.split(" ");
								if (thirdLine.indexOf("SGST")<0) {
									if (thirdLineCols.length  ==4 ) {
										hsn = thirdLineCols[0];
										qty = thirdLineCols[1];
										mrp = thirdLineCols[2];
										unitCost = thirdLineCols[3];
									} else if (thirdLineCols.length==3 ) {
										qty = thirdLineCols[0];
										mrp = thirdLineCols[1];
										unitCost = thirdLineCols[2];
									}
								}
								
							}
							if (thirdLine.indexOf("SGST")>=0) {
								thirdLineCols = thirdLine.split(" ");
								//System.out.println("3 line SGST Col :" + thirdLineCols.length+ " - " + thirdLineCols[3] );
								sgst = thirdLineCols[3].substring(thirdLineCols[3].indexOf("-")+1,thirdLineCols[3].indexOf("%"));
								//System.out.println("SGST is :" + sgst);
								qty = thirdLineCols[0]; mrp = thirdLineCols[1]; unitCost =   thirdLineCols[2];
								totalAmout = thirdLineCols[4];
								if (fourthLine.indexOf("CGST")>=0) {
									if (fourthLine.indexOf("CGST") == 0) {
										cgst = fourthLine.substring(fourthLine.indexOf("-")+1,fourthLine.indexOf("%") );
										
									} else {
										fourthLineCols = fourthLine.split(" ");
										cgst = fourthLineCols[fourthLineCols.length-1];
										cgst = cgst.substring(cgst.indexOf("-")+1,cgst.indexOf("%"));
										hsn =  fourthLineCols[fourthLineCols.length-2];
										itemName = itemName.trim() + " ";
										for (int k=0;k< fourthLineCols.length-3;k++ ) {
											itemName = itemName + fourthLineCols[k] + " ";
										}
									}
								}
								
							} else if (fourthLine.indexOf("SGST")>=0) {
								fourthLineCols = fourthLine.split(" ");
								//System.out.println("4 line SGST Col :" + fourthLineCols.length+ " - " + fourthLineCols[0] );
								sgst = fourthLineCols[0].substring(fourthLineCols[0].indexOf("-")+1,fourthLineCols[0].indexOf("%") );
								//System.out.println("SGST is :" + sgst);
								totalAmout = fourthLineCols[1];
								if (fifthLine.indexOf("CGST")>=0) {
									if (fifthLine.indexOf("CGST") == 0) {
										cgst = fifthLine.substring(fifthLine.indexOf("-")+1,fifthLine.indexOf("%") );
										try {
											if (Integer.parseInt(sixthLine) > 0) {
											} 
										} catch(Exception er) {
											itemName = itemName.trim() + " " + sixthLine;
 										}

									} else {
										fifthLineCols = fifthLine.split(" ");
										cgst = fifthLineCols[fifthLineCols.length-1];
										cgst = cgst.substring(cgst.indexOf("-")+1,cgst.indexOf("%") );
										itemName = itemName + " ";
										for (int k=0;k< fifthLineCols.length-1;k++ ) {
											itemName = itemName + fifthLineCols[k] + " ";
										}
									}
								}
							} 
							
							lineData = firstLine + columnDelim + itemName.trim()  + columnDelim + ean + columnDelim + hsn + columnDelim + sgst + columnDelim + cgst + columnDelim + qty + columnDelim + mrp + columnDelim + unitCost + columnDelim + totalAmout ;
							//lineData = String.join(" ",cols).trim();
							retPoData = retPoData + lineData + "\n";
							//i = i + 1;
							System.out.println(lineData);
						}
						
						
					} catch(Exception e) {
						//e.printStackTrace();
					}
			
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
				 
				 poItem.setArticleNo(cols[0]);
				 poItem.setMaterialDesc( cols[1] ); 
				 poItem.setEanNo(cols[2]);
				 poItem.setHsnNo(cols[3]);
				 poItem.setCgst(Float.parseFloat(cols[4].trim()));
				 poItem.setSgst(Float.parseFloat(cols[5].trim()));
				 poItem.setQuantity(Float.parseFloat(cols[6].trim()));
				 poItem.setMrp(Float.parseFloat(cols[7].trim()));
				 poItem.setBasicCost(Float.parseFloat(cols[8].trim()));
				 poItem.setSalesTax(poItem.getCgst() + poItem.getSgst() );
				 poItem.setUom("");
				 poItem.setTotalBaseValue(Float.parseFloat(cols[9].trim().replaceAll(",","")));
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
