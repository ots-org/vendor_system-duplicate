package com.fuso.enterprise.ots.srv.api.service.request;

import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;

public class AddorUpdateProductBORequest {
	private ProductDetails requestData;

	public ProductDetails getRequestData() {
		return requestData;
	}

	public void setRequestData(ProductDetails requestData) {
		this.requestData = requestData;
	}

	
}
