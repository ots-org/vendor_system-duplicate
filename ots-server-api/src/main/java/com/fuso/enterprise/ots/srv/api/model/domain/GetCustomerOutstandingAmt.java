package com.fuso.enterprise.ots.srv.api.model.domain;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetCustomerOutstandingAmt {
	
	@Size(max = 10)
	private String customerId;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	
	
}
