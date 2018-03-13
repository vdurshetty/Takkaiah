package com.takkaiah.db.dto;

public class ItemWithCustomerMaringPercent {
	
	int itemId;
	String eanCode;
	String hsnCode;
	String custArticleCode;
	String itemDesc;
	float itemPercent;
	float mrp;
	float custMarginPercent;
	public ItemWithCustomerMaringPercent() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getEanCode() {
		return eanCode;
	}
	public void setEanCode(String eanCode) {
		this.eanCode = eanCode;
	}
	public float getMrp() {
		return mrp;
	}
	public void setMrp(float mrp) {
		this.mrp = mrp;
	}
	public String getHsnCode() {
		return hsnCode;
	}
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public float getItemPercent() {
		return itemPercent;
	}
	public void setItemPercent(float itemPercent) {
		this.itemPercent = itemPercent;
	}
	public float getCustMarginPercent() {
		return custMarginPercent;
	}
	public void setCustMarginPercent(float custMarginPercent) {
		this.custMarginPercent = custMarginPercent;
	}
	public String getCustArticleCode() {
		return custArticleCode;
	}
	public void setCustArticleCode(String custArticleCode) {
		this.custArticleCode = custArticleCode;
	}
	
	

}
