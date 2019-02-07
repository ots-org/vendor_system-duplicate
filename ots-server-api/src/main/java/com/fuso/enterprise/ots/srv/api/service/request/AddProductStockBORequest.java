package com.fuso.enterprise.ots.srv.api.service.request;

import com.fuso.enterprise.ots.srv.api.model.domain.AddProductStock;

public class AddProductStockBORequest {

	private AddProductStock request;

	public AddProductStock getRequest() {
		return request;
	}

	public void setRequest(AddProductStock request) {
		this.request = request;
	}
}
