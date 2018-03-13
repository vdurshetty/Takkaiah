package com.takkaiah.pdf.vo;

public class PurchaseOrderItems {
	
	int itemNo;
	String articleNo;
	String eanNo;
	String hsnNo;
	String materialDesc;
	float cQty;
	float quantity;
	String uom;
	float mrp;
	float basicCost;
	float salesTax;
	float totalBaseValue;
	float cgst;
	float sgst;
	
	/**
	 * 
	 */
	public PurchaseOrderItems() {
		super();
	}
	public int getItemNo() {
		return itemNo;
	}
	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}
	public String getArticleNo() {
		return articleNo;
	}
	public void setArticleNo(String articleNo) {
		this.articleNo = articleNo;
	}
	public String getEanNo() {
		return eanNo;
	}
	public void setEanNo(String eanNo) {
		this.eanNo = eanNo;
	}
	public String getMaterialDesc() {
		return materialDesc;
	}
	public void setMaterialDesc(String materialDesc) {
		this.materialDesc = materialDesc;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public float getMrp() {
		return mrp;
	}
	public void setMrp(float mrp) {
		this.mrp = mrp;
	}
	public float getBasicCost() {
		return basicCost;
	}
	public void setBasicCost(float basicCost) {
		this.basicCost = basicCost;
	}
	public float getSalesTax() {
		return salesTax;
	}
	public void setSalesTax(float salesTax) {
		this.salesTax = salesTax;
	}
	public float getTotalBaseValue() {
		return totalBaseValue;
	}
	public void setTotalBaseValue(float totalBaseValue) {
		this.totalBaseValue = totalBaseValue;
	}
	public float getCgst() {
		return cgst;
	}
	public void setCgst(float cgst) {
		this.cgst = cgst;
	}
	public float getSgst() {
		return sgst;
	}
	public void setSgst(float sgst) {
		this.sgst = sgst;
	}
	public String getHsnNo() {
		return hsnNo;
	}
	public void setHsnNo(String hsnNo) {
		this.hsnNo = hsnNo;
	}
	public float getcQty() {
		return cQty;
	}
	public void setcQty(float cQty) {
		this.cQty = cQty;
	}
	

}
