package com.takkaiah.pdf.core.process;

import java.util.Hashtable;

import com.takkaiah.pdf.core.POReaderList;
import com.takkaiah.pdf.core.ProcessMultiPPOs;
import com.takkaiah.pdf.vo.POReadStatus;

public class HyperCityPDFListRead implements POReaderList {
	

	@Override
	public Hashtable<String, POReadStatus> getPODetails(String[] poFiles) throws Exception {
		ProcessMultiPPOs processPOs = new ProcessMultiPPOs();
		return processPOs.processPOFiles(poFiles,new BigBazarPDFRead());
	}
}
