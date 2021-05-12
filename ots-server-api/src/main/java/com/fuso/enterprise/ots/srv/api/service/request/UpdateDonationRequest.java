package com.fuso.enterprise.ots.srv.api.service.request;

import com.fuso.enterprise.ots.srv.api.model.domain.DonationModel;

public class UpdateDonationRequest {
	private DonationModel request;

	public DonationModel getRequest() {
		return request;
	}

	public void setRequest(DonationModel request) {
		this.request = request;
	}
}
