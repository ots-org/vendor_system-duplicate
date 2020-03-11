package com.fuso.enterprise.ots.srv.api.service.request;

import com.fuso.enterprise.ots.srv.api.model.domain.GetBillOrderIdModel;

public class GetBillByOrderIdBORequest {

	private GetBillOrderIdModel request;

	public GetBillOrderIdModel getRequest() {
		return request;
	}

	public void setRequest(GetBillOrderIdModel request) {
		this.request = request;
	}
	
}
