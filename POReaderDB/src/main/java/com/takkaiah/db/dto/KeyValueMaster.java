package com.takkaiah.db.dto;

public class KeyValueMaster {
	
	private int id;
	private String text;
	
	public KeyValueMaster(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
	@Override
	public String toString(){
		return text;
	}
	

}
