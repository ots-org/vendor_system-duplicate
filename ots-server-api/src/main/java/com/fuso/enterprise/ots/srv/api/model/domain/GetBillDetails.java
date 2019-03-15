package com.fuso.enterprise.ots.srv.api.model.domain;

import java.util.List;

public class GetBillDetails {

	private String BillId;
	
	private String BillNumber;
	
	private String BillAmount;
	
	private String BillAmountRecived;	

	private String BillGenerated;
	
	private String BillStatus;
	
	private List<BillOrderModelDetails> billOrderModelDetailsList;

	public List<BillOrderModelDetails> getBillOrderModelDetailsList() {
		return billOrderModelDetailsList;
	}

	public void setBillOrderModelDetailsList(List<BillOrderModelDetails> billOrderModelDetailsList) {
		this.billOrderModelDetailsList = billOrderModelDetailsList;
	}

	public String getBillId() {
		return BillId;
	}

	public void setBillId(String billId) {
		BillId = billId;
	}

	public String getBillNumber() {
		return BillNumber;
	}

	public void setBillNumber(String billNumber) {
		BillNumber = billNumber;
	}

	public String getBillAmount() {
		return BillAmount;
	}

	public void setBillAmount(String billAmount) {
		BillAmount = billAmount;
	}

	public String getBillGenerated() {
		return BillGenerated;
	}

	public void setBillGenerated(String billGenerated) {
		BillGenerated = billGenerated;
	}

	public String getBillStatus() {
		return BillStatus;
	}

	public void setBillStatus(String billStatus) {
		BillStatus = billStatus;
	}
	public String getBillAmountRecived() {
		return BillAmountRecived;
	}

	public void setBillAmountRecived(String billAmountRecived) {
		BillAmountRecived = billAmountRecived;
	}

}
