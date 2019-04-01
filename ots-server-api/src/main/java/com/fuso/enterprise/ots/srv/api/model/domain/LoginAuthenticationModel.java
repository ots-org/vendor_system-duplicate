package com.fuso.enterprise.ots.srv.api.model.domain;

public class LoginAuthenticationModel {

	private String EmailId;
	
	private String Password;
	
	private String DevicrToken;

	public String getDevicrToken() {
		return DevicrToken;
	}

	public void setDevicrToken(String devicrToken) {
		DevicrToken = devicrToken;
	}

	public String getEmailId() {
		return EmailId;
	}

	public void setEmailId(String emailId) {
		EmailId = emailId;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}
	
	
}
