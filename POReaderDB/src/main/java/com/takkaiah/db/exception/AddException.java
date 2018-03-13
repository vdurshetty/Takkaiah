package com.takkaiah.db.exception;

public class AddException extends Exception {
	private static final long serialVersionUID = 1L;
	String msg;
	
	public AddException(String msg){
		super(msg);
		this.msg = msg;
	}
	public String toString(){
		return msg;
	}
}