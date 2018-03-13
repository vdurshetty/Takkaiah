package com.takkaiah.db.exception;

public class UpdateException extends Exception {
	private static final long serialVersionUID = 1L;
	String msg;
	
	public UpdateException(String msg){
		super(msg);
		this.msg = msg;
	}
	public String toString(){
		return msg;
	}
}


