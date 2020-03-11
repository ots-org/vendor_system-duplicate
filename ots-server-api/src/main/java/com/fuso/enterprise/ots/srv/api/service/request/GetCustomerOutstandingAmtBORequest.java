package com.fuso.enterprise.ots.srv.api.service.request;

import com.fuso.enterprise.ots.srv.api.model.domain.GetCustomerOutstandingAmt;

public class GetCustomerOutstandingAmtBORequest {
	
	private GetCustomerOutstandingAmt requestData;

	public GetCustomerOutstandingAmt getRequestData() {
		return requestData;
	}

	public void setRequestData(GetCustomerOutstandingAmt requestData) {
		this.requestData = requestData;
	}

}
