package com.fuso.enterprise.ots.srv.api.service.request;

import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetailsRequest;

public class AddOrUpdateOrderProductBOrequest {
	
	private OrderDetailsRequest request;

	public OrderDetailsRequest getRequest() {
		return request;
	}

	public void setRequest(OrderDetailsRequest request) {
		this.request = request;
	}
}
