package com.takkaiah.pdf.core.process;


import com.takkaiah.pdf.core.POReader;
import com.takkaiah.pdf.exception.POFormatException;
import com.takkaiah.pdf.read.MaxHypermarketPDF;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;

public class MaxHypermarketPDFRead implements POReader {
	
	 
	 public MaxHypermarketPDFRead(){

	 }

	@Override
	public PurchaseOrderInfo getPODetails(String poFile) throws POFormatException {
		MaxHypermarketPDF sPDF = new MaxHypermarketPDF();
		 return sPDF.getPODetails(poFile);
	}

}
