
	package com.takkaiah.pdf.core.process;


	import com.takkaiah.pdf.core.POReader;
import com.takkaiah.pdf.exception.POFormatException;
import com.takkaiah.pdf.read.SpencersPDF;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;

	public class SpencersPDFRead implements POReader {
		
	 
		 public SpencersPDFRead(){

		 }

		@Override
		public PurchaseOrderInfo getPODetails(String poFile) throws POFormatException {
			SpencersPDF sPDF = new SpencersPDF();
			 return sPDF.getPODetails(poFile);
		}
	
	}
