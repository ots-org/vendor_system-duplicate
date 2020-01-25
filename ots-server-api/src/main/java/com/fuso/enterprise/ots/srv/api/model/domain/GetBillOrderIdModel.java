package com.fuso.enterprise.ots.srv.api.model.domain;

public class GetBillOrderIdModel {

	private String BillNumber;

	private String pdf;
	
	public String getBillNumber() {
		return BillNumber;
	}

	public void setBillNumber(String billNumber) {
		BillNumber = billNumber;
	}

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}
		
}
