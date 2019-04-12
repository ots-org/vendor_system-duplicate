package com.fuso.enterprise.ots.srv.api.service.response;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.CustomerOutstanding;

public class OutstandingCustomerResponse {
	List<CustomerOutstanding> customerOutstandingList;

	public List<CustomerOutstanding> getCustomerOutstandingList() {
		return customerOutstandingList;
	}

	public void setCustomerOutstandingList(List<CustomerOutstanding> customerOutstandingList) {
		this.customerOutstandingList = customerOutstandingList;
	}
	
	

}
