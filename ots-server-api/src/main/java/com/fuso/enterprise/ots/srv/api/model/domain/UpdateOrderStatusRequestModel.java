package com.fuso.enterprise.ots.srv.api.model.domain;

public class UpdateOrderStatusRequestModel {

	private String OrderId;
	
	private String Status;

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
	
	
}
