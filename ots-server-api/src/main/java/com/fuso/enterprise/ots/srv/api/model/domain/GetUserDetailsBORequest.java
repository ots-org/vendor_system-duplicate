package com.fuso.enterprise.ots.srv.api.model.domain;

public class GetUserDetailsBORequest {
	
	String searchKey;
	String searchvalue;
	
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
