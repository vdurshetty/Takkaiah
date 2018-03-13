package com.takkaiah.pdf.core;

import com.takkaiah.pdf.core.process.AdityaBirlaPDFListRead;
import com.takkaiah.pdf.core.process.HyperCityPDFListRead;
import com.takkaiah.pdf.core.process.MaxHypermarketPDFListRead;
import com.takkaiah.pdf.core.process.ReliancePDFListRead;
import com.takkaiah.pdf.core.process.SpencersPDFListRead;

public class POReaderListFactory {
	
		
	 //use getPOReader method to get object of type POReader based on the customer selected 
	   public static POReaderList getPOReader(int poFormat, boolean readNew){
		   
	      if(poFormat <=0 ){
	         return null;
	      }		
	      if(poFormat==POFormats.ReliancePO){
	         return new ReliancePDFListRead();
	      } else if(poFormat==POFormats.SpencerPO){
		         return new SpencersPDFListRead();
		  }  else if(poFormat==POFormats.AdityaBirlaPO){
		         return new AdityaBirlaPDFListRead(readNew);
		  } else if(poFormat==POFormats.HiperCityPO){
		         return new HyperCityPDFListRead();
		  } else if(poFormat==POFormats.MaxHyperMarketPO){
		         return new MaxHypermarketPDFListRead();
		  } 
  
	      return null;
	      
	   }
	
	
}
