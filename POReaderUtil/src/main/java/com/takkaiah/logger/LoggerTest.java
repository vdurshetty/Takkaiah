package com.takkaiah.logger;

import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;

//import org.apache.logging.log4j.ThreadContext;




public class LoggerTest {
	
	private static POReaderLogger log = POReaderLogger.getLogger(LoggerTest.class.getName());
	
		
	public static void main(String a[]) throws Exception{
		System.out.println("Log File Name is:" + POReaderReadEnv.getEnvValue(POReaderEnvProp.LoggerFileName));
		
	  for (int i=0;i<100;i++){
		log.debug("Hello");
	  log.debug("My Test Log Message");
	  }
	  System.out.println("Venu1 Logger Test Aysnc Logger - End");
	}

}
