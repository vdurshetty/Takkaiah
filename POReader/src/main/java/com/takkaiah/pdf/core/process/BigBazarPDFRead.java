package com.takkaiah.pdf.core.process;

import com.takkaiah.pdf.core.POReader;
import com.takkaiah.pdf.exception.POFormatException;
import com.takkaiah.pdf.read.BigBazarPDF;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;

public class BigBazarPDFRead implements POReader {
	
	 
	 public BigBazarPDFRead(){

	 }

	@Override
	public PurchaseOrderInfo getPODetails(String poFile) throws POFormatException {
		 BigBazarPDF bPDF = new BigBazarPDF();
		 return bPDF.getPODetails(poFile);
	}
}
