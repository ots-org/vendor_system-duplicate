package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionHistory;
import com.fuso.enterprise.ots.srv.api.service.request.AddSubscriptionBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetSubscriptionDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.response.SubscriptionDetailsResponse;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscriptionOrderHistory;

public interface SubscriptionOrderHistoryDao {
	
	SubscriptionHistory addSubscription(AddSubscriptionBORequest addSubscriptionBORequest);

	SubscriptionDetailsResponse getUserSubscriptionDetails(GetSubscriptionDetailsRequest subscriptionDetailsRequest);

}
