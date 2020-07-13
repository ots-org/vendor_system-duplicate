package com.fuso.enterprise.ots.srv.api.service.response;

import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionUserOrderModel;

public class CurrentSubscriptionBOResponse {
	private SubscriptionUserOrderModel currentSubscriptionModel;

	public SubscriptionUserOrderModel getCurrentSubscriptionModel() {
		return currentSubscriptionModel;
	}

	public void setCurrentSubscriptionModel(SubscriptionUserOrderModel currentSubscriptionModel) {
		this.currentSubscriptionModel = currentSubscriptionModel;
	}

	
}
