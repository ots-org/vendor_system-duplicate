package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.service.request.AddSubscriptionBORequest;

public interface SubscriptionServiceDao  {

	 String addUserSubscription(AddSubscriptionBORequest addSubscriptionBORequest);
}
