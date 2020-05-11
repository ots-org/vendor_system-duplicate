package com.fuso.enterprise.ots.srv.api.model.domain;

public class SubscriptionRoleOrderModel{
	
	private String subscriptionOrderDetailsId;
	
	private String orderHistoryId;
	
	private String roleId;
	
	private String userId;
	
	private String addOns;

	public String getSubscriptionOrderDetailsId() {
		return subscriptionOrderDetailsId;
	}

	public void setSubscriptionOrderDetailsId(String subscriptionOrderDetailsId) {
		this.subscriptionOrderDetailsId = subscriptionOrderDetailsId;
	}

	public String getOrderHistoryId() {
		return orderHistoryId;
	}

	public void setOrderHistoryId(String orderHistoryId) {
		this.orderHistoryId = orderHistoryId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddOns() {
		return addOns;
	}

	public void setAddOns(String addOns) {
		this.addOns = addOns;
	}
	
	
}
