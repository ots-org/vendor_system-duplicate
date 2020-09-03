package com.fuso.enterprise.ots.srv.api.service.request;

import java.util.List;
import com.fuso.enterprise.ots.srv.api.model.domain.AddDonationModelRequest;

public class AddDonationtoRequest {

	private List<AddDonationModelRequest> request;

	public List<AddDonationModelRequest> getRequest() {
		return request;
	}

	public void setRequest(List<AddDonationModelRequest> request) {
		this.request = request;
	}
}
