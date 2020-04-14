package com.fuso.enterprise.ots.srv.api.model.domain;

import java.util.List;

public class SubscriptionUserOrderModel {

	private String orderId;
	
	private String subscriptionName;
	
	private String userId;
	
	private String dateOfBilling;
	
	private String Status;
	
	private String mode;
	
	private String orderCost;

	private List<SubscriptionRoleMappingModel> subscriptionRoleMappingModelList;
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSubscriptionName() {
		return subscriptionName;
	}

	public void setSubscriptionName(String subscriptionName) {
		this.subscriptionName = subscriptionName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDateOfBilling() {
		return dateOfBilling;
	}

	public void setDateOfBilling(String dateOfBilling) {
		this.dateOfBilling = dateOfBilling;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getOrderCost() {
		return orderCost;
	}

	public void setOrderCost(String orderCost) {
		this.orderCost = orderCost;
	}

	public List<SubscriptionRoleMappingModel> getSubscriptionRoleMappingModelList() {
		return subscriptionRoleMappingModelList;
	}

	public void setSubscriptionRoleMappingModelList(List<SubscriptionRoleMappingModel> subscriptionRoleMappingModelList) {
		this.subscriptionRoleMappingModelList = subscriptionRoleMappingModelList;
	}
	
	
}
