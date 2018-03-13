package com.takkaiah.pdf.core;

import com.takkaiah.pdf.exception.POFormatException;
import com.takkaiah.pdf.vo.PurchaseOrderInfo;

public interface POReader {
	
	public PurchaseOrderInfo getPODetails(String poFile) throws POFormatException; 
	
}
