package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionHistory;
import com.fuso.enterprise.ots.srv.api.service.response.SubscriptionDetailsResponse;

public interface SubscriptionOrderDetailsDao {
	SubscriptionDetailsResponse addSubscription(SubscriptionHistory subscriptionHistory);
	SubscriptionDetailsResponse getSubscriptionDetails(SubscriptionDetailsResponse subscriptionDetailsResponse);
}
