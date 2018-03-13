package com.takkaiah.logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.message.MessageFactory;

import com.takkaiah.env.POReaderEnvProp;
import com.takkaiah.env.POReaderReadEnv;


// This class is used to create the Apache logger context based on the configurations file.
/*
 * Required Log4j2.x jar files
 * log4j2-api-2.2.jar
 * log4j2-core-2.2.jar
 */
public class POReaderLoggerContext
{

	private static LoggerContext ctx;
	static 
	{
		buildContext();
	}

	private static void buildContext()
	{
		final String LoggerConfigFileName = POReaderReadEnv.getEnvValue(POReaderEnvProp.LoggerFileName);
		try
		{
			File initialFile = new File(LoggerConfigFileName);
		    InputStream targetStream = new FileInputStream(initialFile);
			ctx = (LoggerContext)LogManager.getContext(true);
			//InputStream is = POReaderLoggerContext.class.getResourceAsStream(LoggerConfigFileName);
			
		    
			ConfigurationSource source = new ConfigurationSource(targetStream);
			//Configuration config = ConfigurationFactory.getInstance().getConfiguration(source);
			Configuration config = ConfigurationFactory.getInstance().getConfiguration(ctx,source);
			ctx.stop();
			ctx.start(config);
		}
		catch(Exception er)
		{
			System.out.println((new StringBuilder("Unable to build logger context from config file :")).append(LoggerConfigFileName).toString());
			er.printStackTrace();
		}
	}

	public static LoggerContext getContext()
	{
		startContext();
		return ctx;
	}

	public static MessageFactory getMessageFactory(String fqcn){
		return getLogger(fqcn).getMessageFactory();
	}

	/*private static void stopContext()
	    {
	        if(ctx.isStarted())
	            ctx.stop();
	    }*/

	private static void startContext()
	{
		if(ctx == null || !ctx.isStarted())
			buildContext();
	}

	private static Logger getLogger(String fqcn)
	{
		return ctx.getLogger(fqcn);
	}



}  // End of the class
