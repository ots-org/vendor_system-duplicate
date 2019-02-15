package com.fuso.enterprise.ots.srv.api.service.request;

import javax.validation.constraints.Size;

public class ApproveRegistrationBODomainRequest {
	
	@Size(max = 100)
	private String registrationId;

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

}
