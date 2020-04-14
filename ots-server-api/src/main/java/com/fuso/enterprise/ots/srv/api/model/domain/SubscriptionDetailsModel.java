package com.fuso.enterprise.ots.srv.api.model.domain;

import java.util.List;

public class SubscriptionDetailsModel {
	
	private String historId;
	
	private String userId ;
	
	private String orderCost;
	
	private String PaymentDate;
	
	private String transactionId;
	
	private String mode;
	
	private List<SubscriptionRoleOrderModel> userRole;
	
	private List<SubscriptionUserOrderModel> subscriptionUserOrderModel;

	public String getHistorId() {
		return historId;
	}

	public void setHistorId(String historId) {
		this.historId = historId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderCost() {
		return orderCost;
	}

	public void setOrderCost(String orderCost) {
		this.orderCost = orderCost;
	}

	public String getPaymentDate() {
		return PaymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		PaymentDate = paymentDate;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	
	public List<SubscriptionUserOrderModel> getSubscriptionUserOrderModel() {
		return subscriptionUserOrderModel;
	}

	public void setSubscriptionUserOrderModel(List<SubscriptionUserOrderModel> subscriptionUserOrderModel) {
		this.subscriptionUserOrderModel = subscriptionUserOrderModel;
	}

	public List<SubscriptionRoleOrderModel> getUserRole() {
		return userRole;
	}

	public void setUserRole(List<SubscriptionRoleOrderModel> userRole) {
		this.userRole = userRole;
	}
	
	
}
