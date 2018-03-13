package com.takkaiah.logger;
import org.apache.logging.log4j.core.Logger;


// This is a logger wrapper class and is required to be used in all the classes where the logging is needed.
public class POReaderLogger extends Logger{

	private static final long serialVersionUID = -3322235225498326070L;

	private POReaderLogger(String fqcn){
		super(POReaderLoggerContext.getContext(),fqcn, POReaderLoggerContext.getMessageFactory(fqcn) );
	}
	 
	public static POReaderLogger getLogger(String fqcn){
		return new POReaderLogger(fqcn);
	}
}