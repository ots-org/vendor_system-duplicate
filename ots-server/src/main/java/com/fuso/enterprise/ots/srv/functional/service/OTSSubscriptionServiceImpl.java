package com.fuso.enterprise.ots.srv.functional.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionHistory;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSSubscriptionService;
import com.fuso.enterprise.ots.srv.api.service.request.AddSubscriptionBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetSubscriptionDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.response.SubscriptionDetailsResponse;
import com.fuso.enterprise.ots.srv.server.dao.SubscriptionOrderDao;
import com.fuso.enterprise.ots.srv.server.dao.SubscriptionOrderDetailsDao;
import com.fuso.enterprise.ots.srv.server.dao.SubscriptionOrderHistoryDao;
import com.fuso.enterprise.ots.srv.server.dao.SubscriptionServiceDao;

@Service
@Transactional
public class OTSSubscriptionServiceImpl implements OTSSubscriptionService  {
	
	private SubscriptionOrderDao subscriptionOrderDao;
	private SubscriptionOrderDetailsDao subscriptionOrderDetailsDao;
	private SubscriptionOrderHistoryDao subscriptionOrderHistoryDao;
	@Inject
	public OTSSubscriptionServiceImpl(SubscriptionOrderDao subscriptionOrderDao ,
			SubscriptionOrderDetailsDao subscriptionOrderDetailsDao,
			SubscriptionOrderHistoryDao subscriptionOrderHistoryDao) {;
		this.subscriptionOrderDao = subscriptionOrderDao;
		this.subscriptionOrderDetailsDao = subscriptionOrderDetailsDao;
		this.subscriptionOrderHistoryDao = subscriptionOrderHistoryDao;
	}

	@Override
	public SubscriptionHistory addUserSubscription(AddSubscriptionBORequest addSubscriptionBORequest) { 
		SubscriptionHistory subscriptionHistory = new SubscriptionHistory();
		try {
			subscriptionHistory = subscriptionOrderHistoryDao.addSubscription(addSubscriptionBORequest);
			subscriptionOrderDetailsDao.addSubscription(subscriptionHistory);
			subscriptionHistory = subscriptionOrderDao.subScriptionOrder(subscriptionHistory);
		}catch(Exception e) {
			
		}
		return subscriptionHistory;
	}

	@Override
	public SubscriptionDetailsResponse getSubscriptionDetails(
			GetSubscriptionDetailsRequest subscriptionDetailsRequest) {
		SubscriptionDetailsResponse subscriptionDetailsResponse = new SubscriptionDetailsResponse();
 		subscriptionDetailsResponse = subscriptionOrderHistoryDao.getUserSubscriptionDetails(subscriptionDetailsRequest);
 		subscriptionDetailsResponse = subscriptionOrderDetailsDao.getSubscriptionDetails(subscriptionDetailsResponse);
		return subscriptionDetailsResponse;
	}
	
	
	
}
