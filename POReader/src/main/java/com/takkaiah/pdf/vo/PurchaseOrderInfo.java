package com.takkaiah.pdf.vo;

import java.util.Date;
import java.util.List;

public class PurchaseOrderInfo {
	
	String poNumber;
	Date poDate;
	Date shipmentDate;
	String customerName;
	Date deliveryDate;
	Date poExpiryDate;
	boolean outStation = false;
	
	List<PurchaseOrderItems> poItems;
	

	/**
	 * 
	 */
	public PurchaseOrderInfo() {
		super();
	}
	
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public Date getPoDate() {
		return poDate;
	}
	public void setPoDate(Date poDate) {
		this.poDate = poDate;
	}
	public Date getShipmentDate() {
		return shipmentDate;
	}
	public void setShipmentDate(Date shipmentDate) {
		this.shipmentDate = shipmentDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	
	public Date getPoExpiryDate() {
		return poExpiryDate;
	}

	public void setPoExpiryDate(Date poExpiryDate) {
		this.poExpiryDate = poExpiryDate;
	}

	public List<PurchaseOrderItems> getPoItems() {
		return poItems;
	}

	public void setPoItems(List<PurchaseOrderItems> poItems) {
		this.poItems = poItems;
	}

	public boolean isOutStation() {
		return outStation;
	}

	public void setOutStation(boolean outStation) {
		this.outStation = outStation;
	}
	
	
	
}
