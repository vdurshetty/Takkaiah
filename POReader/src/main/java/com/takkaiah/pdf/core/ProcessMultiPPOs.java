package com.takkaiah.pdf.core;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.takkaiah.logger.POReaderLogger;
import com.takkaiah.pdf.vo.POReadStatus;

public class ProcessMultiPPOs {
	
	POReaderLogger log = POReaderLogger.getLogger(ProcessMultiPPOs.class.getName());
	
	public  Hashtable<String, POReadStatus> processPOFiles(String poFiles[], POReader poReader){
		
		Hashtable<String,POReadStatus> table = new Hashtable<>();
		
	    ExecutorService pool = Executors.newFixedThreadPool(poFiles.length);
	    Set<Future<POReadStatus>> set = new HashSet<Future<POReadStatus>>();
	    for (int i=0;i<poFiles.length;i++) {
	      Callable<POReadStatus> callable = new POThread(poFiles[i], poReader);  
	      Future<POReadStatus> future = pool.submit(callable);
	      set.add(future);
	    }
	    for (Future<POReadStatus> future : set) {
	    	try{
		    	POReadStatus poRead =  (POReadStatus) future.get();
		    	table.put(poRead.getPoFile(), poRead);
	    	}catch(Exception e){
	    		log.error("Error in Multi Process call : " + e.getMessage());
	    	}
	   }
		
		return table;
	}
}
