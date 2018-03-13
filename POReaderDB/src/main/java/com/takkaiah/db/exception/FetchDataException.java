package com.takkaiah.db.exception;

public class FetchDataException extends Exception{
	private static final long serialVersionUID = 1L;
	String msg;
	
	public FetchDataException(String msg){
		super(msg);
		this.msg = msg;
	}
	public String toString(){
		return msg;
	}
}
