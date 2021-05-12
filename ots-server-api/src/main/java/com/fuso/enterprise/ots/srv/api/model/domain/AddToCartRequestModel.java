package com.fuso.enterprise.ots.srv.api.model.domain;

public class AddToCartRequestModel {
	
	private Integer otsCartId;
	
	private Integer customerId;

	private Integer productId;
	
	private Integer otsCartQty;
	
	private Integer distributorId;
	
	private Float custlat;
	
	private Float custlon;

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

	public Integer getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Integer distributorId) {
		this.distributorId = distributorId;
	}

	public Float getCustlat() {
		return custlat;
	}

	public void setCustlat(Float custlat) {
		this.custlat = custlat;
	}

	public Float getCustlon() {
		return custlon;
	}

	public void setCustlon(Float custlon) {
		this.custlon = custlon;
	}

	
	
}
