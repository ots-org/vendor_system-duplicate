package com.fuso.enterprise.ots.srv.api.service.response;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;

public class ProductDetailsBOResponse {
	
	private String userId;
	
	List<ProductDetails> ProductDetails;

	public List<ProductDetails> getProductDetails() {
		return ProductDetails;
	}

	public void setProductDetails(List<ProductDetails> productDetails) {
		ProductDetails = productDetails;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	

}
