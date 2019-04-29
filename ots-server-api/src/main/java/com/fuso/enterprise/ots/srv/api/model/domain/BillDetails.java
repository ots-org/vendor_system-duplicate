package com.fuso.enterprise.ots.srv.api.model.domain;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Size;

public class BillDetails {
	
	 @Size(max = 20)
	 private Integer billId;
	 
	 @Size(max = 20)
	 private String billAmount;
	 
	 @Size(max = 20)
	 private String billAmountReceived;
	 
	 @Size(max = 20)
	 private String billGenerated;
	 
	 @Size(max = 20)
	 private String IGST;
	 
	 @Size(max = 20)
	 private String CGST;
	 
	 @Size(max = 20)
	 private String outstandingAmount;
	 
	 @Size(max = 20)
	 private Integer customerId;
	 
	 @Size(max = 20)
	 private List<ListOfOrderId> orderId;

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
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

	public List<ListOfOrderId> getOrderId() {
		return orderId;
	}

	public void setOrderId(List<ListOfOrderId> orderId) {
		this.orderId = orderId;
	}

	public String getIGST() {
		return IGST;
	}

	public void setIGST(String iGST) {
		IGST = iGST;
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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	

}
