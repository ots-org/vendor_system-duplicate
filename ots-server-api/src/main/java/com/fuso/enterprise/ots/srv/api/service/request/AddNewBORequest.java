package com.fuso.enterprise.ots.srv.api.service.request;

import com.fuso.enterprise.ots.srv.api.model.domain.AddUserDetails;


public class AddNewBORequest {

	private AddUserDetails requestData;

	public AddUserDetails getRequestData() {
		return requestData;
	}

	public void setRequestData(AddUserDetails requestData) {
		this.requestData = requestData;
	}
	
	
}
