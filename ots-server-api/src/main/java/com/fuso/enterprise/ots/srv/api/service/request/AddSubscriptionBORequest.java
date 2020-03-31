package com.fuso.enterprise.ots.srv.api.service.request;

import java.util.List;
import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionRoleDetails;

public class AddSubscriptionBORequest {
	
	private String mode;
	private String status;
	private String transactionId;
	private String orderCost;
	private String userId;
	private List<SubscriptionRoleDetails> subscriptionRoleDetails;
	
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<SubscriptionRoleDetails> getSubscriptionRoleDetails() {
		return subscriptionRoleDetails;
	}
	public void setSubscriptionRoleDetails(List<SubscriptionRoleDetails> subscriptionRoleDetails) {
		this.subscriptionRoleDetails = subscriptionRoleDetails;
	}
	public String getOrderCost() {
		return orderCost;
	}
	public void setOrderCost(String orderCost) {
		this.orderCost = orderCost;
	}
	
}
