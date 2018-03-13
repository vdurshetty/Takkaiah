package com.takkaiah.pdf.vo;

public class POReadStatus {
	
	boolean success;
	String poFile;
	String  errorMsg;
	PurchaseOrderInfo poInfo;
	
	public POReadStatus(){
		
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public PurchaseOrderInfo getPoInfo() {
		return poInfo;
	}

	public void setPoInfo(PurchaseOrderInfo poInfo) {
		this.poInfo = poInfo;
	}

	public String getPoFile() {
		return poFile;
	}

	public void setPoFile(String poFile) {
		this.poFile = poFile;
	}

	 
	
}
