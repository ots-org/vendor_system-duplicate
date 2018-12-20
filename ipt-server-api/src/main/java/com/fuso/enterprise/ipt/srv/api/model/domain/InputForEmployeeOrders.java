package com.fuso.enterprise.ipt.srv.api.model.domain;

import javax.validation.constraints.Size;

public class InputForEmployeeOrders {
	
	@Size(max = 50)
    private String userId;
	
	@Size(max = 50)
	private String currentDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
    

}
