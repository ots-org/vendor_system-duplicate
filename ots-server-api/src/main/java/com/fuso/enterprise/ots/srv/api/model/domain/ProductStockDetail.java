package com.fuso.enterprise.ots.srv.api.model.domain;

public class ProductStockDetail {
	
	long productId;
	
	String productName;
	
	long otsProductOpenBalance;
	
	long otsProductStockAddition;
	
	long otsProductOrderDelivered;
	
	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public long getOtsProductOpenBalance() {
		return otsProductOpenBalance;
	}

	public void setOtsProductOpenBalance(long otsProductOpenBalance) {
		this.otsProductOpenBalance = otsProductOpenBalance;
	}

	public long getOtsProductStockAddition() {
		return otsProductStockAddition;
	}

	public void setOtsProductStockAddition(long otsProductStockAddition) {
		this.otsProductStockAddition = otsProductStockAddition;
	}

	public long getOtsProductOrderDelivered() {
		return otsProductOrderDelivered;
	}

	public void setOtsProductOrderDelivered(long otsProductOrderDelivered) {
		this.otsProductOrderDelivered = otsProductOrderDelivered;
	}
	
	

	
}
