package com.fuso.enterprise.ots.srv.api.service.request;

import com.fuso.enterprise.ots.srv.api.model.domain.CustomerOutstandingDetails;

public class CustomerOutstandingBORequest {
	
  private CustomerOutstandingDetails requestData;

public CustomerOutstandingDetails getRequestData() {
	return requestData;
}

public void setRequestData(CustomerOutstandingDetails requestData) {
	this.requestData = requestData;
}
  
}
