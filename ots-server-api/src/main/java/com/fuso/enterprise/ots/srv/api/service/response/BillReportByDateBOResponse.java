package com.fuso.enterprise.ots.srv.api.service.response;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.ListOfBillId;

public class BillReportByDateBOResponse {
	
	List<ListOfBillId> billNumber;

	public List<ListOfBillId> getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(List<ListOfBillId> billNumber) {
		this.billNumber = billNumber;
	}

	
	
}
