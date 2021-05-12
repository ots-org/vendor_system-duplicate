package com.fuso.enterprise.ots.srv.api.service.request;

import com.fuso.enterprise.ots.srv.api.model.domain.CustomerDetailsForLogin;

public class CustomerDetailsForLoginRequest {

	private CustomerDetailsForLogin requestData;

	public CustomerDetailsForLogin getRequestData() {
		return requestData;
	}

	public void setRequestData(CustomerDetailsForLogin requestData) {
		this.requestData = requestData;
	}

}
