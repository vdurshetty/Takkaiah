package com.takkaiah.email;

import javax.activation.DataHandler;

public class EmailAttachment {
	
	private String fileName;
	
	private DataHandler dataHandler;
	
	public EmailAttachment(){
		
	}

	/**
	 * @param fileName
	 * @param dataHandler
	 */
	public EmailAttachment(String fileName, DataHandler dataHandler) {
		super();
		this.fileName = fileName;
		this.dataHandler = dataHandler;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public DataHandler getDataHandler() {
		return dataHandler;
	}

	public void setDataHandler(DataHandler dataHandler) {
		this.dataHandler = dataHandler;
	}
	
	
	

}
