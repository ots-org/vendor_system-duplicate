package com.fuso.enterprise.ots.srv.api.service.response;

import java.math.BigDecimal;

public class GetcartListResponse {

	private String productName;
	
	private String productImage;
	
	private BigDecimal productPrice;
	
	private Integer otsCartQty;
	
	private BigDecimal totalPrice;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public Integer getOtsCartQty() {
		return otsCartQty;
	}

	public void setOtsCartQty(Integer otsCartQty) {
		this.otsCartQty = otsCartQty;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	
}
