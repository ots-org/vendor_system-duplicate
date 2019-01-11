package com.fuso.enterprise.ots.srv.api.service.response;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;

public class UserDataBOResponse {
	
	List<UserDetails> userdetails;

	public List<UserDetails> getUserdetails() {
		return userdetails;
	}

	public void setUserdetails(List<UserDetails> userdetails) {
		this.userdetails = userdetails;
	}
	
}
