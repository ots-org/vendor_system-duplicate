package com.fuso.enterprise.ots.srv.api.model.domain;

import java.sql.Date;

public class GetListOfOrderRequestModel {

	private String UserId;
	
	private String Role;
	
	private Date StartDate;
	
	private Date EndDate;

	public Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}

	public Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getRole() {
		return Role;
	}

	public void setRole(String role) {
		Role = role;
	}
	
}
