package com.fuso.enterprise.ots.srv.api.service.request;

import com.fuso.enterprise.ots.srv.api.model.domain.CloseOrderModelRequest;

public class CloseOrderBORequest {

	private CloseOrderModelRequest request;

	public CloseOrderModelRequest getRequest() {
		return request;
	}

	public void setRequest(CloseOrderModelRequest request) {
		this.request = request;
	}
}
