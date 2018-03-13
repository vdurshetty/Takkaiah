

	package com.takkaiah.pdf.core.process;


	import com.takkaiah.pdf.core.POReader;
import com.takkaiah.pdf.exception.POFormatException;
import com.takkaiah.pdf.read.HyperCityPDF;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;

	public class HyperCityPDFRead implements POReader {
		
		 
		 public HyperCityPDFRead(){

		 }

		@Override
		public PurchaseOrderInfo getPODetails(String poFile) throws POFormatException {
			HyperCityPDF sPDF = new HyperCityPDF();
			 return sPDF.getPODetails(poFile);
		}
	
	}
