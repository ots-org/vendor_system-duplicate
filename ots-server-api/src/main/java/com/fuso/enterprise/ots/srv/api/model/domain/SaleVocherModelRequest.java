package com.fuso.enterprise.ots.srv.api.model.domain;

import java.util.List;

public class SaleVocherModelRequest {

	private String OrderId;
	
	private String orderCost;
	
	private String amountReceived;

	private String CustomerId;
	
	private String outstandingAmount;
	
	private List<OrderProductListRequest> orderProductlist;

	
	
	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getOrderCost() {
		return orderCost;
	}

	public void setOrderCost(String orderCost) {
		this.orderCost = orderCost;
	}

	public String getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(String amountReceived) {
		this.amountReceived = amountReceived;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}

	public String getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(String outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

	public List<OrderProductListRequest> getOrderProductlist() {
		return orderProductlist;
	}

	public void setOrderProductlist(List<OrderProductListRequest> orderProductlist) {
		this.orderProductlist = orderProductlist;
	}
	
	
}
