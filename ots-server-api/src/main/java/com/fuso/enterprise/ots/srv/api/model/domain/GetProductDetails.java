package com.fuso.enterprise.ots.srv.api.model.domain;

public class GetProductDetails {
	String searchKey;
	String searchvalue;
	String distributorId;
	String customerId;
	String status;
	String maxRange;
	String minRange;
	
	public String getMaxRange() {
		return maxRange;
	}
	public void setMaxRange(String maxRange) {
		this.maxRange = maxRange;
	}
	public String getMinRange() {
		return minRange;
	}
	public void setMinRange(String minRange) {
		this.minRange = minRange;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	public String getSearchvalue() {
		return searchvalue;
	}
	public void setSearchvalue(String searchvalue) {
		this.searchvalue = searchvalue;
	}
}
