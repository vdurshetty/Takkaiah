package com.takkaiah.pdf.core;

import java.util.concurrent.Callable;

import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.pdf.vo.POReadStatus;

public class POThread implements Callable<POReadStatus> {
	String poFile;
	POReader poReader;
	POReaderLogger log = POReaderLogger.getLogger(POThread.class.getName());
	public POThread(String poFile, POReader poReader){
		this.poFile = poFile;
		this.poReader = poReader;
	}
	
    public POReadStatus call() throws Exception {
    	POReadStatus poStatus = new POReadStatus();
    	try{
    		poStatus.setPoFile(poFile);
    		poStatus.setPoInfo(poReader.getPODetails(poFile));
    		poStatus.setSuccess(true);
    	} catch(Exception e){
    		log.error("Error in thread call : " + e.getMessage());
    		poStatus.setSuccess(false);
    		poStatus.setErrorMsg(e.getMessage());
    	} 
        return poStatus;
    }
}
