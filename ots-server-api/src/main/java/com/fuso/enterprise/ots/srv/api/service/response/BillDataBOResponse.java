package com.fuso.enterprise.ots.srv.api.service.response;

import com.fuso.enterprise.ots.srv.api.model.domain.BillOrderModelDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetBillDetails;

public class BillDataBOResponse {

	private GetBillDetails getBillDetails;

	public GetBillDetails getGetBillDetails() {
		return getBillDetails;
	}

	public void setGetBillDetails(GetBillDetails getBillDetails) {
		this.getBillDetails = getBillDetails;
	}
	
}
