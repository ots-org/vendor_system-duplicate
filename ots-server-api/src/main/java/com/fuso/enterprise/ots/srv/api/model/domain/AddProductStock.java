package com.fuso.enterprise.ots.srv.api.model.domain;

public class AddProductStock {

	private String ProductStockQty; 
	
	private String ProductStockStatus;
	
	private String UsersId; 
	
	private String ProductId;
	
	public String getProductStockQty() {
		return ProductStockQty;
	}
	public void setProductStockQty(String productStockQty) {
		ProductStockQty = productStockQty;
	}
	public String getProductStockStatus() {
		return ProductStockStatus;
	}
	public void setProductStockStatus(String productStockStatus) {
		ProductStockStatus = productStockStatus;
	}
	
	public String getUsersId() {
		return UsersId;
	}
	public void setUsersId(String usersId) {
		UsersId = usersId;
	}
	public String getProductId() {
		return ProductId;
	}
	public void setProductId(String productId) {
		ProductId = productId;
	}
}
