package com.fuso.enterprise.ots.srv.api.service.request;

import javax.validation.constraints.NotNull;

public class UserRoleIdBORequest {

	@NotNull(message = "userRoleId not be empty")
	private String userRoleId;

	public String getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}
}
