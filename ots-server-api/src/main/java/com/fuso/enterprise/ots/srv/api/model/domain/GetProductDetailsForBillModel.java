package com.fuso.enterprise.ots.srv.api.model.domain;

import java.util.List;

public class GetProductDetailsForBillModel {
	private List<String> orderId;

	public List<String> getOrderId() {
		return orderId;
	}

	public void setOrderId(List<String> orderId) {
		this.orderId = orderId;
	}
}
