package com.takkaiah.pdf.core.process;

import com.takkaiah.pdf.core.POReader;
import com.takkaiah.pdf.exception.POFormatException;
import com.takkaiah.pdf.read.ReliancePDF;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;

public class ReliancePDFRead implements POReader {
	
	 
	 public ReliancePDFRead(){

	 }

	@Override
	public PurchaseOrderInfo getPODetails(String poFile) throws POFormatException {
		 ReliancePDF rPDF = new ReliancePDF();
		 return rPDF.fetchPODetails(poFile);
	}
}
