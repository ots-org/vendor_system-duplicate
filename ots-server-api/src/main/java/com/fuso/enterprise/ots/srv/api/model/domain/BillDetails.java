package com.fuso.enterprise.ots.srv.api.model.domain;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Size;

public class BillDetails {
	
	 @Size(max = 20)
	 private Integer billId;
	 
	 @Size(max = 20)
	 private String billNumber;
	 
	 @Size(max = 20)
	 private String billAmount;
	 
	 @Size(max = 20)
	 private String billAmountReceived;
	 
	 @Size(max = 20)
	 private String billGenerated;
	 
	 @Size(max = 20)
	 private String billStatus;
	 
	 @Size(max = 20)
	 private List<ListOfOrderId> orderId;

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
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

	public List<ListOfOrderId> getOrderId() {
		return orderId;
	}

	public void setOrderId(List<ListOfOrderId> orderId) {
		this.orderId = orderId;
	}

}
