package com.fuso.enterprise.ots.srv.api.model.domain;

public class SubscriptionRoleMappingModel {
	private String roleMappingId;
	
	private String subscriptionMappingId;
	
	private String RoleId;
	
	private String userCount;
	
	private String subscriptionOrderId;

	public String getRoleMappingId() {
		return roleMappingId;
	}

	public void setRoleMappingId(String roleMappingId) {
		this.roleMappingId = roleMappingId;
	}

	public String getSubscriptionMappingId() {
		return subscriptionMappingId;
	}

	public void setSubscriptionMappingId(String subscriptionMappingId) {
		this.subscriptionMappingId = subscriptionMappingId;
	}

	public String getRoleId() {
		return RoleId;
	}

	public void setRoleId(String roleId) {
		RoleId = roleId;
	}

	public String getUserCount() {
		return userCount;
	}

	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}

	public String getSubscriptionOrderId() {
		return subscriptionOrderId;
	}

	public void setSubscriptionOrderId(String subscriptionOrderId) {
		this.subscriptionOrderId = subscriptionOrderId;
	}
	
	
}
