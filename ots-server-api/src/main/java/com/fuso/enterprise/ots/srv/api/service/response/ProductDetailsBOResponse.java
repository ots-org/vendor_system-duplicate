package com.fuso.enterprise.ots.srv.api.service.response;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;

public class ProductDetailsBOResponse {
	
	List<ProductDetails> ProductDetails;

	public List<ProductDetails> getProductDetails() {
		return ProductDetails;
	}

	public void setProductDetails(List<ProductDetails> productDetails) {
		ProductDetails = productDetails;
	}

	

}
