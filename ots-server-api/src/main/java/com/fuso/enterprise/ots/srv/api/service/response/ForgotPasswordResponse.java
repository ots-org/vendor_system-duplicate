package com.fuso.enterprise.ots.srv.api.service.response;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;

public class ForgotPasswordResponse {

	private String UserId;
	
	private String otp;

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}



	
}
