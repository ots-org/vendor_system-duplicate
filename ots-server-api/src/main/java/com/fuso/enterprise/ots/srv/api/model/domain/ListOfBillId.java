package com.fuso.enterprise.ots.srv.api.model.domain;

public class ListOfBillId {
	
	String billId;
	String billNumber;
	String billAmount;
	String billAmountReceived;
	String billGenerated;
	String billStatus;
	String iGST;
	String CGST;
	String outstandingAmount;
	
	
	public String getiGST() {
		return iGST;
	}
	public void setiGST(String iGST) {
		this.iGST = iGST;
	}
	public String getCGST() {
		return CGST;
	}
	public void setCGST(String cGST) {
		CGST = cGST;
	}
	public String getOutstandingAmount() {
		return outstandingAmount;
	}
	public void setOutstandingAmount(String outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}
	public String getBillAmountReceived() {
		return billAmountReceived;
	}
	public void setBillAmountReceived(String billAmountReceived) {
		this.billAmountReceived = billAmountReceived;
	}
	public String getBillGenerated() {
		return billGenerated;
	}
	public void setBillGenerated(String billGenerated) {
		this.billGenerated = billGenerated;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	
	
}
