package com.fuso.enterprise.ots.srv.api.service.response;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.CustomerOutstandingDetails;

public class GetCustomerOutstandingAmtBOResponse {
	
	private List<CustomerOutstandingDetails> customerOutstandingAmount;

	public List<CustomerOutstandingDetails> getCustomerOutstandingAmount() {
		return customerOutstandingAmount;
	}

	public void setCustomerOutstandingAmount(List<CustomerOutstandingDetails> customerOutstandingAmount) {
		this.customerOutstandingAmount = customerOutstandingAmount;
	}
    
	
}
