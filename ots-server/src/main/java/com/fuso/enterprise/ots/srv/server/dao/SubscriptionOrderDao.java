package com.fuso.enterprise.ots.srv.server.dao;


import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionHistory;
import com.fuso.enterprise.ots.srv.api.service.request.GetSubscriptionDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.response.CurrentSubscriptionBOResponse;

public interface SubscriptionOrderDao {
	SubscriptionHistory subScriptionOrder(SubscriptionHistory subscriptionHistory);
	CurrentSubscriptionBOResponse getCurrentSubscriptionDetails(
			GetSubscriptionDetailsRequest subscriptionDetailsRequest);
}
