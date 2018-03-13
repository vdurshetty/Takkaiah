package com.takkaiah.db.exception;

public class RecordNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;
	String msg;
	
	public RecordNotFoundException(String msg){
		super(msg);
		this.msg = msg;
	}
	public String toString(){
		return msg;
	}
}
