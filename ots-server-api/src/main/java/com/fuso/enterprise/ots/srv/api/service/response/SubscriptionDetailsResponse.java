package com.fuso.enterprise.ots.srv.api.service.response;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionDetailsModel;

public class SubscriptionDetailsResponse {
	private List<SubscriptionDetailsModel> subscriptionDetails;

	public List<SubscriptionDetailsModel> getSubscriptionDetails() {
		return subscriptionDetails;
	}

	public void setSubscriptionDetails(List<SubscriptionDetailsModel> subscriptionDetails) {
		this.subscriptionDetails = subscriptionDetails;
	}
	
	
}
