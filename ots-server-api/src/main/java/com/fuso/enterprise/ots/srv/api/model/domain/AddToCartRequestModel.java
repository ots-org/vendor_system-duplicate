package com.fuso.enterprise.ots.srv.api.model.domain;

public class AddToCartRequestModel {
	
	private Integer otsCartId;
	
	private Integer customerId;

	private Integer productId;
	
	private Integer otsCartQty;
	
	

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

	public Integer getOtsCartQty() {
		return otsCartQty;
	}

	public void setOtsCartQty(Integer otsCartQty) {
		this.otsCartQty = otsCartQty;
	}

	public Integer getOtsCartId() {
		return otsCartId;
	}

	public void setOtsCartId(Integer otsCartId) {
		this.otsCartId = otsCartId;
	}

	
	
}
