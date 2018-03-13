package com.takkaiah.db.exception;

public class PrimaryKeyException extends Exception{
	private static final long serialVersionUID = 1L;
	String msg;
	
	public PrimaryKeyException(String msg){
		super(msg);
		this.msg = msg;
	}
	public String toString(){
		return msg;
	}
}
