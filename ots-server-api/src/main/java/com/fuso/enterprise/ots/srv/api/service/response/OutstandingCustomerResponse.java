package com.fuso.enterprise.ots.srv.api.service.response;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.CustomerOutstanding;

public class OutstandingCustomerResponse {
	List<CustomerOutstanding> customerOutstandingList;

	private String pdf;
	
	public List<CustomerOutstanding> getCustomerOutstandingList() {
		return customerOutstandingList;
	}

	public void setCustomerOutstandingList(List<CustomerOutstanding> customerOutstandingList) {
		this.customerOutstandingList = customerOutstandingList;
	}

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}
	
	

}
