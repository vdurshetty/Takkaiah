

	package com.takkaiah.pdf.core.process;


	import com.takkaiah.pdf.core.POReader;
import com.takkaiah.pdf.exception.POFormatException;
import com.takkaiah.pdf.read.EasyDayPDF;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;

	public class EasyDayPDFRead implements POReader {
		
		 
		 public EasyDayPDFRead(){

		 }

		@Override
		public PurchaseOrderInfo getPODetails(String poFile) throws POFormatException {
			EasyDayPDF sPDF = new EasyDayPDF();
			 return sPDF.getPODetails(poFile);
		}
	
	}
