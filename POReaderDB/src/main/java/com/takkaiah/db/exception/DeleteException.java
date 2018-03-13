package com.takkaiah.db.exception;

public class DeleteException extends Exception{
	private static final long serialVersionUID = 1L;
	String msg;
	
	public DeleteException(String msg){
		super(msg);
		this.msg = msg;
	}
	public String toString(){
		return msg;
	}
}