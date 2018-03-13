
	package com.takkaiah.pdf.core.process;


	import com.takkaiah.pdf.core.POReader;
import com.takkaiah.pdf.exception.POFormatException;
import com.takkaiah.pdf.read.AdityaBirlaOldPDF;
import com.takkaiah.pdf.read.AdityaBirlaPDF;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;

	public class AdityaBirlaPDFRead implements POReader {
		
		 boolean readNew= true;
		 public AdityaBirlaPDFRead(boolean readNew){
			 
			 this.readNew= readNew;

		 }

		@Override
		public PurchaseOrderInfo getPODetails(String poFile) throws POFormatException {
			if (readNew) {
				AdityaBirlaPDF sPDF = new AdityaBirlaPDF();
				return sPDF.getPODetails(poFile);
			} else {
				AdityaBirlaOldPDF sOldPDF = new AdityaBirlaOldPDF();
				return sOldPDF.getPODetails(poFile);
			}
		}
		
	}
