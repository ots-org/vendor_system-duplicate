package com.fuso.enterprise.ots.srv.api.model.domain;

public class AddScheduler {

	private String DistributorID;
	
	private String customerId;
	
	private String prodcutId;
	
	private String prodcutQty;
	
	private String ScheduleType;
	
	private String StartDate;
	
	private String endDate;
	
	private String SchduleweekDays;

	public String getDistributorID() {
		return DistributorID;
	}

	public void setDistributorID(String distributorID) {
		DistributorID = distributorID;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getProdcutId() {
		return prodcutId;
	}

	public void setProdcutId(String prodcutId) {
		this.prodcutId = prodcutId;
	}

	public String getProdcutQty() {
		return prodcutQty;
	}

	public void setProdcutQty(String prodcutQty) {
		this.prodcutQty = prodcutQty;
	}

	public String getScheduleType() {
		return ScheduleType;
	}

	public void setScheduleType(String scheduleType) {
		ScheduleType = scheduleType;
	}

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSchduleweekDays() {
		return SchduleweekDays;
	}

	public void setSchduleweekDays(String schduleweekDays) {
		SchduleweekDays = schduleweekDays;
	}
	
	
}
