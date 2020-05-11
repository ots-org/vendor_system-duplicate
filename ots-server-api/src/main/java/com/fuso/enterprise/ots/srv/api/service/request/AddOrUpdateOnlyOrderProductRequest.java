package com.fuso.enterprise.ots.srv.api.service.request;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.OrderedProductDetails;

public class AddOrUpdateOnlyOrderProductRequest {

	private List<OrderedProductDetails> ProductList;

	public List<OrderedProductDetails> getProductList() {
		return ProductList;
	}

	public void setProductList(List<OrderedProductDetails> productList) {
		ProductList = productList;
	}
	
}
