package com.fuso.enterprise.ots.srv.api.service.request;

import com.fuso.enterprise.ots.srv.api.model.domain.DonationModel;

public class GetAssgineRequest {
	
	private DonationModel donationModel;

	public DonationModel getDonationModel() {
		return donationModel;
	}

	public void setDonationModel(DonationModel donationModel) {
		this.donationModel = donationModel;
	}
}
