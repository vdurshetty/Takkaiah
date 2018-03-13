package com.takkaiah.pdf.core;

import java.util.Hashtable;

import com.takkaiah.pdf.vo.POReadStatus;

public interface  POReaderList {
	public Hashtable<String, POReadStatus > getPODetails(String poFiles[]) throws Exception; 
}
