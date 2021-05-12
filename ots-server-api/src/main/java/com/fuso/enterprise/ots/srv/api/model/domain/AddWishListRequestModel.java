package com.fuso.enterprise.ots.srv.api.model.domain;

public class AddWishListRequestModel {

	private Integer customerId;
	
	private Integer productId;
	
	private Integer productWishlistId;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getProductWishlistId() {
		return productWishlistId;
	}

	public void setProductWishlistId(Integer productWishlistId) {
		this.productWishlistId = productWishlistId;
	}
	
	
}
