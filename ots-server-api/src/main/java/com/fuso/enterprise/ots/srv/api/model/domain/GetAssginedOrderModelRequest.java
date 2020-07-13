package com.fuso.enterprise.ots.srv.api.model.domain;

public class GetAssginedOrderModelRequest {

	private String EmployeeId;
	
	private String Status;

	public String getEmployeeId() {
		return EmployeeId;
	}

	public void setEmployeeId(String employeeId) {
		EmployeeId = employeeId;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
	
	
	
}
