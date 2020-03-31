package com.fuso.enterprise.ots.srv.api.service.functional;

import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionHistory;
import com.fuso.enterprise.ots.srv.api.service.request.AddSubscriptionBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetSubscriptionDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.response.SubscriptionDetailsResponse;

public interface OTSSubscriptionService {
	SubscriptionHistory addUserSubscription(AddSubscriptionBORequest addSubscriptionBORequest);
	SubscriptionDetailsResponse getSubscriptionDetails(GetSubscriptionDetailsRequest subscriptionDetailsRequest);
	
}
