package com.takkaiah.pdf.core;

import com.takkaiah.pdf.core.process.AdityaBirlaPDFRead;
import com.takkaiah.pdf.core.process.BigBazarPDFRead;
import com.takkaiah.pdf.core.process.EasyDayPDFRead;
import com.takkaiah.pdf.core.process.HyperCityPDFRead;
import com.takkaiah.pdf.core.process.MaxHypermarketPDFRead;
import com.takkaiah.pdf.core.process.ReliancePDFRead;
import com.takkaiah.pdf.core.process.SpencersPDFRead;

public class POReaderFactory {
	
	
	  private POReaderFactory(){
		  
	  }
	
	 //use getPOReader method to get object of type POReader based on the customer selected 
	   public static POReader getPOReader(int poFormat, boolean readNew){
		   
	      if(poFormat <=0 ){
	         return null;
	      }		
	      if(poFormat==POFormats.ReliancePO){
	         return new ReliancePDFRead();

	      }  else  if(poFormat==POFormats.MaxHyperMarketPO){
	         return new MaxHypermarketPDFRead();
	      
	      }  else  if(poFormat==POFormats.AdityaBirlaPO){
	         return new AdityaBirlaPDFRead(readNew);
	         
	      }	else  if(poFormat==POFormats.SpencerPO){
		     return new SpencersPDFRead(); 
	      
	      } else  if(poFormat==POFormats.HiperCityPO){
		     return new HyperCityPDFRead();
		     
	      } else  if(poFormat==POFormats.BigBazarPO){
			     return new BigBazarPDFRead();
			    		 
		  }else  if(poFormat==POFormats.EasyDayPO){
			     return new EasyDayPDFRead();
	    		 
		  }
	      return null;
	   }
	
	
}
