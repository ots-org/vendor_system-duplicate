package com.fuso.enterprise.ots.srv.api.model.domain;

import java.util.List;

public class DonationResponseByStatus {
	
	private List<ProductDetails> productList;

	public List<ProductDetails> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductDetails> productList) {
		this.productList = productList;
	}
}
