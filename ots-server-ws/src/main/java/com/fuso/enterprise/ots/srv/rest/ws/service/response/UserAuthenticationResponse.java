package com.fuso.enterprise.ots.srv.rest.ws.service.response;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;

public class UserAuthenticationResponse {
	
	private UserDetails userdetails;
	
	public UserDetails getUserdetails() {
		return userdetails;
	}
	public void setUserdetails(UserDetails userdetails) {
		this.userdetails = userdetails;
	}
	
}
