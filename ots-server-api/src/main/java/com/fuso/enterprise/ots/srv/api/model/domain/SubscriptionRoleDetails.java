package com.fuso.enterprise.ots.srv.api.model.domain;

public class SubscriptionRoleDetails {
	
	private String userRoleOrderId;
	
	private String roleId;
	
	private String countToadd;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getCountToadd() {
		return countToadd;
	}

	public void setCountToadd(String countToadd) {
		this.countToadd = countToadd;
	}

	public String getUserRoleOrderId() {
		return userRoleOrderId;
	}

	public void setUserRoleOrderId(String userRoleOrderId) {
		this.userRoleOrderId = userRoleOrderId;
	}


}